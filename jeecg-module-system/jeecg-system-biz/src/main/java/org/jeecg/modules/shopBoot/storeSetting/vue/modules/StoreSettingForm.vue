<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="开店功能" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="openStore">
              <j-dict-select-tag type="radio" v-model="model.openStore" dictCode="openStore" placeholder="请选择开店功能" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺管理地址" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="manageAddress">
              <a-input v-model="model.manageAddress" placeholder="请输入店铺管理地址"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺初始密码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="initialPasswd">
              <a-input v-model="model.initialPasswd" placeholder="请输入店铺初始密码"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="默认服务距离" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="serviceDistance">
              <a-input-number v-model="model.serviceDistance" placeholder="请输入默认服务距离" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺首页推荐店铺" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="storeCut">
              <j-dict-select-tag type="radio" v-model="model.storeCut" dictCode="storeCut" placeholder="请选择店铺首页推荐店铺" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺欢迎弹窗" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="autoWelcome">
              <j-dict-select-tag type="radio" v-model="model.autoWelcome" dictCode="autoWelcome" placeholder="请选择店铺欢迎弹窗" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺列表默认排序" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="defaultSort">
              <j-dict-select-tag type="radio" v-model="model.defaultSort" dictCode="defaultSort" placeholder="请选择店铺列表默认排序" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开店邀请图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="inviteFigure">
              <j-image-upload isMultiple  v-model="model.inviteFigure" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="分享图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="coverPlan">
              <j-image-upload isMultiple  v-model="model.coverPlan" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="海报图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="posters">
              <j-image-upload isMultiple  v-model="model.posters" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开店宣传图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="openPublicityPic">
              <j-image-upload isMultiple  v-model="model.openPublicityPic" ></j-image-upload>
            </a-form-model-item>
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
    name: 'StoreSettingForm',
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
          queryById: "/storeSetting/storeSetting/queryById"
        }
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
       //备份model原始值
      this.modelDefault = JSON.parse(JSON.stringify(this.model));
    },
    methods: {
      add () {
        this.edit(this.modelDefault);
      },
      edit (record) {
        this.model = Object.assign({}, record);
        this.visible = true;
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