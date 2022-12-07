<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" style='margin-top: 30px' slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="系统名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" style="width: 60%" placeholder="请输入系统名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="系统描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="description">
              <a-textarea v-model="model.description" style="width: 60%" rows="4" placeholder="请输入系统描述" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="版权信息" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="copyrightInfo">
              <a-input v-model="model.copyrightInfo" style="width: 60%" placeholder="请输入版权信息"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备案号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beian">
              <a-input v-model="model.beian" style="width: 60%" placeholder="请输入备案号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="备案地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="beianUrl">
              <a-input v-model="model.beianUrl" style="width: 60%" placeholder="请输入备案地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="登录页logo" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="loginLogo">
              <j-image-upload isMultiple  v-model="model.loginLogo" style="width: 60%" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="系统内页logo" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="systemRelatedLogo">
              <j-image-upload isMultiple  v-model="model.systemRelatedLogo" style="width: 60%" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="标签logo" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="labelLogo">
              <j-image-upload isMultiple  v-model="model.labelLogo" style="width: 60%" ></j-image-upload>
            </a-form-model-item>
          </a-col>
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
    name: 'SysBackSettingFormPage',
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
           name: [
              { required: true, message: '请输入系统名称!'},
           ],
           description: [
              { required: true, message: '请输入系统描述!'},
           ],
           copyrightInfo: [
              { required: true, message: '请输入版权信息!'},
           ],
           loginLogo: [
              { required: true, message: '请输入登录页logo!'},
           ],
           systemRelatedLogo: [
              { required: true, message: '请输入系统内页logo!'},
           ],
           labelLogo: [
              { required: true, message: '请输入标签logo!'},
           ],
        },
        url: {
          add: "/sysBackSetting/sysBackSetting/add",
          edit: "/sysBackSetting/sysBackSetting/edit",
          queryFirstData: "/sysBackSetting/sysBackSetting/queryFirstData"
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