<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="开通类型：0：包年；1：终生" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="openType">
              <j-dict-select-tag type="list" v-model="model.openType" dictCode="store_payment_open_type" placeholder="请选择开通类型：0：包年；1：终生" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开通费用" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="money">
              <a-input-number v-model="model.money" placeholder="请输入开通费用" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="开通时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="startTime">
              <j-date placeholder="请选择开通时间"  v-model="model.startTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="到期时间，如果是终生就为空" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="endTime">
              <j-date placeholder="请选择到期时间，如果是终生就为空"  v-model="model.endTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="支付类型：0：微信；1：支付宝" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="payType">
              <j-dict-select-tag type="list" v-model="model.payType" dictCode="pay_type" placeholder="请选择支付类型：0：微信；1：支付宝" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="支付时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="payTime">
              <j-date placeholder="请选择支付时间"  v-model="model.payTime" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
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
    name: 'StorePaymentForm',
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
          add: "/store.storePayment/storePayment/add",
          edit: "/store.storePayment/storePayment/edit",
          queryById: "/store.storePayment/storePayment/queryById"
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