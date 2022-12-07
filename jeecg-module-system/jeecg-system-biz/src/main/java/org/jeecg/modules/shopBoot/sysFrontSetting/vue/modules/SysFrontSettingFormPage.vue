<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" style='margin-top: 30px' slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="小程序名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="name">
              <a-input v-model="model.name" style="width: 60%" placeholder="请输入小程序名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="小程序描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="description">
              <a-textarea v-model="model.description" style="width: 60%" rows="4" placeholder="请输入小程序描述" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="小程序logo" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="frontLogo">
              <j-image-upload isMultiple  v-model="model.frontLogo" style="width: 60%" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="首页推广图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="homeGeneralizeAddr">
              <j-image-upload isMultiple  v-model="model.homeGeneralizeAddr" style="width: 60%" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="礼包胶囊图" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="giftCapsuleAddr">
              <j-image-upload isMultiple  v-model="model.giftCapsuleAddr" style="width: 60%" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="商品数据来源标签(小程序端)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="goodLabelSmallsoft">
              <j-multi-select-tag type="checkbox" v-model="model.goodLabelSmallsoft" style="width: 60%" dictCode="storeSourceOptions" placeholder="请选择商品数据来源标签(小程序端)" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="商品数据来源标签(app端)" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="goodLabelApp">
              <j-multi-select-tag type="checkbox" v-model="model.goodLabelApp" style="width: 60%" dictCode="storeSourceOptions" placeholder="请选择商品数据来源标签(app端)" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="首页底部推荐类型" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="indexBottomRecommend">
              <j-dict-select-tag type="radio" v-model="model.indexBottomRecommend" style="width: 60%" dictCode="indexBottomRecommend" placeholder="请选择首页底部推荐类型" />
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
    name: 'SysFrontSettingFormPage',
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
            indexBottomRecommend:"0",
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
              { required: true, message: '请输入小程序名称!'},
           ],
           frontLogo: [
              { required: true, message: '请输入小程序logo!'},
           ],
           homeGeneralizeAddr: [
              { required: true, message: '请输入首页推广图!'},
           ],
           giftCapsuleAddr: [
              { required: true, message: '请输入礼包胶囊图!'},
           ],
        },
        url: {
          add: "/sysFrontSetting/sysFrontSetting/add",
          edit: "/sysFrontSetting/sysFrontSetting/edit",
          queryFirstData: "/sysFrontSetting/sysFrontSetting/queryFirstData"
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