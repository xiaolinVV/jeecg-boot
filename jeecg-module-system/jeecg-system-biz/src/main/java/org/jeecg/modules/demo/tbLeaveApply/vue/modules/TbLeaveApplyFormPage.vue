<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" style='margin-top: 30px' slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="请假人" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" style="width: 60%" placeholder="请输入请假人"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开始时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="startTime">
              <j-date placeholder="请选择开始时间"  v-model="model.startTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 60%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="请假天数" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="days">
              <a-input-number v-model="model.days" placeholder="请输入请假天数" style="width: 60%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="请假事由" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="content">
              <a-textarea v-model="model.content" style="width: 60%" rows="4" placeholder="请输入请假事由" />
            </a-form-model-item>
          </a-col>
        <a-divider></a-divider>
         <a-col :span="24" style="width: 100%;text-align: center;">
            <a-button icon="check" style="width: 126px" type="primary" @click="submitForm">保 存</a-button>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
  </a-spin>
</template>

<script>

  import { httpAction, getAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'

  export default {
    name: 'TbLeaveApplyFormPage',
    components: {
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    data () {
      return {
        model:{
         },
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        confirmLoading: false,
        validatorRules: {
        },
        url: {
          add: "/tbLeaveApply/tbLeaveApply/add",
          edit: "/tbLeaveApply/tbLeaveApply/edit",
          queryFirstData: "/tbLeaveApply/tbLeaveApply/queryFirstData"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      }
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
      //初始化表单数据
      this.showFormData();
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      //初始化表单数据
      showFormData(){
          getAction(this.url.queryFirstData).then((res)=>{
            if(res.success){
              this.edit (res.result);
            }
          });
      },
      submitForm () {
        const that = this;
        // 触发表单验证
        this.$refs.form.validate(valid => {
          if (valid) {
            that.confirmLoading = true;
            let httpurl = '';
            let method = '';
            if(!this.model.id){
              httpurl+=this.url.add;
              method = 'post';
            }else{
              httpurl+=this.url.edit;
               method = 'put';
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                that.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
            })
          }
         
        })
      },
    }
  }
</script>