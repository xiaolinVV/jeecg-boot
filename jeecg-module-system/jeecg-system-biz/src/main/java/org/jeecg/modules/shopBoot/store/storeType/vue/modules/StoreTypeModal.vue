<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    switchFullscreen
    @ok="handleOk"
    @cancel="handleCancel"
    cancelText="关闭">
    <a-spin :spinning="confirmLoading">
      <a-form-model ref="form" :model="model" :rules="validatorRules">
        <a-form-model-item label="分类名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="typeName">
          <a-input v-model="model.typeName" placeholder="请输入分类名称" ></a-input>
        </a-form-model-item>
        <a-form-model-item label="分类级别" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="level">
          <j-dict-select-tag type="list" v-model="model.level"  dictCode="store_type_level" placeholder="请选择分类级别" />
        </a-form-model-item>
        <a-form-model-item label="分类图片" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="logoAddr">
          <j-image-upload isMultiple  v-model="model.logoAddr" ></j-image-upload>
        </a-form-model-item>
        <a-form-model-item label="排序" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sort">
          <a-input-number v-model="model.sort" placeholder="请输入排序" style="width: 100%" />
        </a-form-model-item>
        <a-form-model-item label="福利金抵扣最低值" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="smallWelfarePayments">
          <a-input-number v-model="model.smallWelfarePayments" placeholder="请输入福利金抵扣最低值" style="width: 100%" />
        </a-form-model-item>
        <a-form-model-item label="状态；0：停用；1：启用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
          <j-dict-select-tag type="list" v-model="model.status"  dictCode="store_type_status" placeholder="请选择状态；0：停用；1：启用" />
        </a-form-model-item>
        <a-form-model-item label="停用说明" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="closeExplain">
          <a-textarea v-model="model.closeExplain" rows="4" placeholder="请输入停用说明" />
        </a-form-model-item>
        <a-form-model-item label="父级分类id，0为一级" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="pid">
          <j-tree-select
            ref="treeSelect"
            placeholder="请选择父级分类id，0为一级"
            v-model="model.pid"
            dict="store_type,type_name,id"
            pidField="pid"
            pidValue="0"
            hasChildField="has_child"
            disabled>
          </j-tree-select>
        </a-form-model-item>
        
      </a-form-model>
    </a-spin>
  </j-modal>
</template>

<script>

  import { httpAction } from '@/api/manage'
  import { validateDuplicateValue } from '@/utils/util'
  export default {
    name: "StoreTypeModal",
    components: { 
    },
    data () {
      return {
        title:"操作",
        width:800,
        visible: false,
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
           typeName: [
              { required: true, message: '请输入分类名称!'},
           ],
           logoAddr: [
              { required: true, message: '请输入分类图片!'},
           ],
           sort: [
              { required: true, message: '请输入排序!'},
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'},
           ],
           smallWelfarePayments: [
              { required: true, message: '请输入福利金抵扣最低值!'},
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'},
           ],
           status: [
              { required: true, message: '请输入状态；0：停用；1：启用!'},
           ],
        },
        url: {
          add: "/storeType/storeType/add",
          edit: "/storeType/storeType/edit",
        },
        expandedRowKeys:[],
        pidField:"pid"
     
      }
    },
    created () {
       //备份model原始值
       this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add (obj) {
        this.modelDefault.pid=''
        this.edit(Object.assign(this.modelDefault , obj));
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
      },
      close () {
        this.$emit('close');
        this.visible = false;
        this.$refs.form.clearValidate()
      },
      handleOk () {
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
             if(this.model.id && this.model.id === this.model[this.pidField]){
              that.$message.warning("父级节点不能选择自己");
              that.confirmLoading = false;
              return;
            }
            httpAction(httpurl,this.model,method).then((res)=>{
              if(res.success){
                that.$message.success(res.message);
                this.$emit('ok');
              }else{
                that.$message.warning(res.message);
              }
            }).finally(() => {
              that.confirmLoading = false;
              that.close();
            })
          }else{
             return false
          }
        })
      },
      handleCancel () {
        this.close()
      },
      submitSuccess(formData,flag){
        if(!formData.id){
          let treeData = this.$refs.treeSelect.getCurrTreeData()
          this.expandedRowKeys=[]
          this.getExpandKeysByPid(formData[this.pidField],treeData,treeData)
          this.$emit('ok',formData,this.expandedRowKeys.reverse());
        }else{
          this.$emit('ok',formData,flag);
        }
      },
      getExpandKeysByPid(pid,arr,all){
        if(pid && arr && arr.length>0){
          for(let i=0;i<arr.length;i++){
            if(arr[i].key==pid){
              this.expandedRowKeys.push(arr[i].key)
              this.getExpandKeysByPid(arr[i]['parentId'],all,all)
            }else{
              this.getExpandKeysByPid(pid,arr[i].children,all)
            }
          }
        }
      }
      
      
    }
  }
</script>