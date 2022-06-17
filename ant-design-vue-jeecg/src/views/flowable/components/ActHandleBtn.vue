<style lang="less">
</style>
<template>
  <span>
      <a-button :type="btnType" @click="handle()" >{{text}}</a-button>
      <a-modal :title="modalTaskTitle" v-model="modalTaskVisible" :mask-closable="false" :width="500">

      <div  v-if="modalTaskVisible">
        <div v-if="type==handleType.reApply">
          确认无误并重新提交？
        </div>
        <a-form ref="form" :model="form" :label-width="85" >
          <a-form-item v-if="type!==handleType.reApply" label="处理意见" prop="reason">
            <a-input type="textarea" v-model="form.comment" :rows="4" />
          </a-form-item>
          <div v-show="type==2">
            <a-form-item label="退回节点" prop="targetKey" v-if="returnTaskList.length">
              <a-radio-group v-model="form.targetKey" @change="targetKeyChange">
                <a-radio-button
                  v-for="item in returnTaskList"
                  :key="item.id"
                  :value="item.id"
                >{{item.name}}</a-radio-button>
              </a-radio-group>
            </a-form-item>
            <span v-else>无可退回节点！</span>
          </div>
            <div v-if="form.targetKey !== 'start' && candidateUsers.length">
                <a-form-item label="下个节点审批候选人">
                    <a-select
                        mode="multiple"
                        v-model="candidateUsersSelecteds"
                        style="width: 100%"
                        placeholder="请选择下个节点审批候选人"
                    >
                    <a-select-option v-for="user in candidateUsers" :key="user.username" :value="user.username">
                        {{user.realname}}
                    </a-select-option>
                  </a-select>
                </a-form-item>
            </div>
        </a-form>
      </div>
      <div slot="footer">
        <a-button type="text" @click="modalTaskVisible=false">取消</a-button>
        <a-button type="primary" :loading="submitLoading" @click="handelSubmit">提交</a-button>
      </div>
    </a-modal>
  </span>
</template>

<script>
  import {completeTask, rejectTask, returnList, returnTask} from "@views/flowable/api/todo";

export default {
    name: 'ActHandleBtn',
    components: {},
    props: {
        btnType: { type: String, default: 'link', required: false },
        /* handleType 0通过 1驳回 2退回  */
        type: {
            type: String|Number,
            default: '0',
            required: true
        },
        dataId: {
            type: String,
            default: '',
            required: true
        },
      /*流程变量*/
        variables:{
          type: Object,
          default: ()=>{},
        },
        candidateUsers:{
          type: Array,
          default: ()=>[],
        },
        text: {
            type: String,
            default: '处理',
            required: false
        }
    },
    data() {
        return {
          handleType:{
            // 通过
            pass: 0,
            // 驳回
            back: 1,
            // 退回
            return: 2,
            // 重新提交
            reApply: 3
            },
            returnTaskList: [],
            candidateUsersSelecteds:[],
            modalTaskVisible: false,
            submitLoading: false,
            form: {
              comment:'',
              targetKey:''
            },
            modalTaskTitle: '',

        };
    },
    created() {
    },
    watch: {
    },
    methods: {
        handle() {
          this.form.comment = ''
          this.candidateUsersSelecteds = []
            if (this.type === this.handleType.delegate) {
                // this.delegateTask();
            } else if (this.type === this.handleType.pass) {
                this.passTask();
            } else if (this.type === this.handleType.back) {
                this.backTask();
            } else if(this.type === this.handleType.return){
                this.returnTask();
            } else if(this.type === this.handleType.reApply){
                this.reApply();
            }
            else {
                this.$message.warn('未知类型type，参见 handleType');
            }
        },
        reApply() {
            const v = this;
            this.modalTaskTitle = '确认重新提交';
            this.modalTaskVisible = true;
        },
        passTask() {
            const v = this;
            this.modalTaskTitle = '审批通过';
            this.modalTaskVisible = true;
        },
        backTask() {
          const v = this;
          this.modalTaskTitle = '审批驳回';
          this.modalTaskVisible = true;
        },
        returnTask() {
            const v = this;
            this.modalTaskTitle = '审批退回';
            this.modalTaskVisible = true;
            returnList({dataId:this.dataId}).then(res => {
              this.returnTaskList = res.result||[];
              // console.log(this.returnTaskList)
            })
        },

        handelSubmit() {
            console.log('提交');
            this.submitLoading = true;
            var formData = Object.assign({
                dataId:this.dataId,
                candidateUsers:this.candidateUsersSelecteds,
                values:Object.assign({dataId:this.dataId},this.variables)
            }, this.form);
            if (this.type==this.handleType.reApply){
              formData.comment = '重新提交'
            }
            if (!formData.comment){
              this.$message.error('请输入审批意见！');
              this.submitLoading=false
              return;
            }
            // 有下个节点审批人选择，但是未选
            if (this.candidateUsers.length &&
              this.candidateUsersSelecteds.length==0 &&
              this.form.targetKey !== 'start'
            ){
              this.$message.error('请选择下个节点审批人！');
              this.submitLoading=false
              return;
            }
            if (this.type == this.handleType.reApply || this.type == this.handleType.pass) {
                // 通过
              completeTask(formData).then(res => {
                    this.submitLoading = false;
                    if (res.success) {
                        this.$message.success('操作成功');
                        this.modalTaskVisible = false;
                        this.$emit('success');
                    } else {
                        this.$message.error('操作失败');
                    }
                }).finally(()=>{this.submitLoading=false});
            } else if (this.type == this.handleType.back) {
                // 驳回
                  rejectTask(formData).then(res => {
                        this.submitLoading = false;
                        if (res.success) {
                            this.$message.success('操作成功');
                            this.modalTaskVisible = false;
                            this.$emit('success');
                        } else {
                            this.$message.error('操作失败');
                        }
                    }).finally(()=>{this.submitLoading=false});

            } else if (this.type == this.handleType.return){
              if (!formData.targetKey){
                this.$message.error('请选择退回节点！');
                this.submitLoading=false
                return;
              }
              //退回
              returnTask(formData).then(res => {
                this.submitLoading = false;
                if (res.success) {
                  this.$message.success('操作成功');
                  this.modalTaskVisible = false;
                  this.$emit('success');
                } else {
                  this.$message.error('操作失败');
                }
              }).finally(()=>{this.submitLoading=false});
            }
        },

        targetKeyChange() {
            this.candidateUsersSelecteds = []
            this.$emit('targetKeyChange',this.form.targetKey)
        }
    }

};
</script>
