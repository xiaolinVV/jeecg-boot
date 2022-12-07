<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" style='margin-top: 30px' slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="开店功能" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="openStore">
              <j-dict-select-tag type="radio" v-model="model.openStore" style="width: 60%" dictCode="openStore" placeholder="请选择开店功能" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺管理地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="manageAddress">
              <a-input v-model="model.manageAddress" style="width: 60%" placeholder="请输入店铺管理地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺初始密码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="initialPasswd">
              <a-input v-model="model.initialPasswd" style="width: 60%" placeholder="请输入店铺初始密码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="默认服务距离" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="serviceDistance">
              <a-input-number v-model="model.serviceDistance" placeholder="请输入默认服务距离" style="width: 60%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺首页推荐店铺" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="storeCut">
              <j-dict-select-tag type="radio" v-model="model.storeCut" style="width: 60%" dictCode="storeCut" placeholder="请选择店铺首页推荐店铺" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺欢迎弹窗" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="autoWelcome">
              <j-dict-select-tag type="radio" v-model="model.autoWelcome" style="width: 60%" dictCode="autoWelcome" placeholder="请选择店铺欢迎弹窗" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺列表默认排序" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="defaultSort">
              <j-dict-select-tag type="radio" v-model="model.defaultSort" style="width: 60%" dictCode="defaultSort" placeholder="请选择店铺列表默认排序" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开店邀请图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="inviteFigure">
              <j-image-upload isMultiple  v-model="model.inviteFigure" style="width: 60%" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分享图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="coverPlan">
              <j-image-upload isMultiple  v-model="model.coverPlan" style="width: 60%" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="海报图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="posters">
              <j-image-upload isMultiple  v-model="model.posters" style="width: 60%" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开店宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="openPublicityPic">
              <j-image-upload isMultiple  v-model="model.openPublicityPic" style="width: 60%" ></j-image-upload>
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
    name: 'StoreSettingFormPage',
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
            openStore:"1",
            storeCut:"0",
            autoWelcome:"1",
            defaultSort:"0",
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
           openStore: [
              { required: true, message: '请输入开店功能!'},
           ],
           manageAddress: [
              { required: true, message: '请输入店铺管理地址!'},
           ],
           initialPasswd: [
              { required: true, message: '请输入店铺初始密码!'},
           ],
           serviceDistance: [
              { required: true, message: '请输入默认服务距离!'},
              { pattern: /^-?\d+\.?\d*$/, message: '请输入数字!'},
           ],
           inviteFigure: [
              { required: true, message: '请输入开店邀请图!'},
           ],
           coverPlan: [
              { required: true, message: '请输入分享图!'},
           ],
           posters: [
              { required: true, message: '请输入海报图!'},
           ],
           openPublicityPic: [
              { required: true, message: '请输入开店宣传图!'},
           ],
        },
        url: {
          add: "/storeSetting/storeSetting/add",
          edit: "/storeSetting/storeSetting/edit",
          queryFirstData: "/storeSetting/storeSetting/queryFirstData"
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