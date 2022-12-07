<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" style='margin-top: 30px' slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="标题" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="title">
              <a-input v-model="model.title" style="width: 60%" placeholder="请输入标题"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="英文名" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="english">
              <a-input v-model="model.english" style="width: 60%" placeholder="请输入英文名"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="内容" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="contant">
              <j-editor v-model="model.contant" style="width: 60%" />
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
    name: 'SysAgreementFormPage',
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
           title: [
              { required: true, message: '请输入标题!'},
           ],
           english: [
              { required: true, message: '请输入英文名!'},
           ],
           contant: [
              { required: true, message: '请输入内容!'},
           ],
        },
        url: {
          add: "/sysAgreement/sysAgreement/add",
          edit: "/sysAgreement/sysAgreement/edit",
          queryFirstData: "/sysAgreement/sysAgreement/queryFirstData"
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