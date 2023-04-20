<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="24">
            <a-form-model-item label="订单id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderListId">
              <a-input v-model="model.orderListId" placeholder="请输入订单id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="订单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderNo">
              <a-input v-model="model.orderNo" placeholder="请输入订单号"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderType">
              <a-input v-model="model.orderType" placeholder="请输入订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="订单商品记录id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderGoodRecordId">
              <a-input v-model="model.orderGoodRecordId" placeholder="请输入订单商品记录id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="会员id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="memberId">
              <a-input v-model="model.memberId" placeholder="请输入会员id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="店铺用户id" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="sysUserId">
              <a-input v-model="model.sysUserId" placeholder="请输入店铺用户id"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="退款类型 0=仅退款 1=退货退款 2=换货" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="refundType">
              <a-input v-model="model.refundType" placeholder="请输入退款类型 0=仅退款 1=退货退款 2=换货"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="退款原因" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="refundReason">
              <a-input v-model="model.refundReason" placeholder="请输入退款原因"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="拒绝退款理由" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="refundExplain">
              <a-input v-model="model.refundExplain" placeholder="请输入拒绝退款理由"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="申请说明" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="remarks">
              <a-input v-model="model.remarks" placeholder="请输入申请说明"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="售后状态 0=待处理 1=待买家退回 2=换货中 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="status">
              <a-input v-model="model.status" placeholder="请输入售后状态 0=待处理 1=待买家退回 2=换货中 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="商品主图相对地址（以json的形式存储多张）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="goodMainPicture">
              <a-input v-model="model.goodMainPicture" placeholder="请输入商品主图相对地址（以json的形式存储多张）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="平台商品id（只做对象映射）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="goodListId">
              <a-input v-model="model.goodListId" placeholder="请输入平台商品id（只做对象映射）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="商品规格id（只做对象映射）" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="goodSpecificationId">
              <a-input v-model="model.goodSpecificationId" placeholder="请输入商品规格id（只做对象映射）"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="商品名称" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="goodName">
              <a-input v-model="model.goodName" placeholder="请输入商品名称"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="规格名称，按照顺序逗号隔开" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="goodSpecification">
              <a-input v-model="model.goodSpecification" placeholder="请输入规格名称，按照顺序逗号隔开"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="退款凭证图片，按照顺序逗号隔开" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="refundCertificate">
              <a-input v-model="model.refundCertificate" placeholder="请输入退款凭证图片，按照顺序逗号隔开"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="退款金额" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="refundPrice">
              <a-input-number v-model="model.refundPrice" placeholder="请输入退款金额" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="退款数量" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="refundAmount">
              <a-input-number v-model="model.refundAmount" placeholder="请输入退款数量" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="logisticsCompany">
              <a-input v-model="model.logisticsCompany" placeholder="请输入物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="24">
            <a-form-model-item label="快递单号" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="trackingNumber">
              <a-input v-model="model.trackingNumber" placeholder="请输入快递单号"  ></a-input>
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
    name: 'OrderRefundListForm',
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
          add: "/order/orderRefundList/add",
          edit: "/order/orderRefundList/edit",
          queryById: "/order/orderRefundList/queryById"
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