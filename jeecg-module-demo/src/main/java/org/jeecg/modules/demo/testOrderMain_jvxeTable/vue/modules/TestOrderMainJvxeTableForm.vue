<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <!-- 主表单区域 -->
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="12" >
            <a-form-model-item label="订单编码" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderCode">
              <a-input v-model="model.orderCode" placeholder="请输入订单编码" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12" >
            <a-form-model-item label="下单时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="orderDate">
              <j-date placeholder="请选择下单时间" v-model="model.orderDate" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="12" >
            <a-form-model-item label="描述" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="descc">
              <a-input v-model="model.descc" placeholder="请输入描述" ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="12" >
            <a-form-model-item label="下拉多选" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="xiala">
              <j-multi-select-tag type="list_multi" v-model="model.xiala" dictCode="sex" placeholder="请选择下拉多选" />
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-form-container>
      <!-- 子表单区域 -->
    <a-tabs v-model="activeKey" @change="handleChangeTabs">
      <a-tab-pane tab="订单产品明细" :key="refKeys[0]" :forceRender="true">
        <j-vxe-table
          keep-source
          :ref="refKeys[0]"
          :loading="testOrderProductJvxeTableTable.loading"
          :columns="testOrderProductJvxeTableTable.columns"
          :dataSource="testOrderProductJvxeTableTable.dataSource"
          :maxHeight="300"
          :disabled="formDisabled"
          :rowNumber="true"
          :rowSelection="true"
          :toolbar="true"
          />
      </a-tab-pane>
    </a-tabs>
  </a-spin>
</template>

<script>

  import { getAction } from '@/api/manage'
  import { JVxeTableModelMixin } from '@/mixins/JVxeTableModelMixin.js'
  import { JVXETypes } from '@/components/jeecg/JVxeTable'
  import { getRefPromise,VALIDATE_FAILED} from '@/components/jeecg/JVxeTable/utils/vxeUtils.js'
  import { validateDuplicateValue } from '@/utils/util'
  import JFormContainer from '@/components/jeecg/JFormContainer'

  export default {
    name: 'TestOrderMainJvxeTableForm',
    mixins: [JVxeTableModelMixin],
    components: {
      JFormContainer,
    },
    data() {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
        model:{
         },
        // 新增时子表默认添加几行空数据
        addDefaultRowNum: 1,
        validatorRules: {
        },
        refKeys: ['testOrderProductJvxeTable', ],
        tableKeys:['testOrderProductJvxeTable', ],
        activeKey: 'testOrderProductJvxeTable',
        // 订单产品明细
        testOrderProductJvxeTableTable: {
          loading: false,
          dataSource: [],
          columns: [
            {
              title: '产品名字',
              key: 'productName',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '价格',
              key: 'price',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules: [{ pattern: "n", message: "${title}格式不正确" }],
            },
            {
              title: '数量',
              key: 'num',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
              validateRules: [{ pattern: "n", message: "${title}格式不正确" }],
            },
            {
              title: '产品类型',
              key: 'proType',
              type: JVXETypes.select,
              options:[],
              dictCode:"sex",
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
            {
              title: '描述',
              key: 'descc',
               type: JVXETypes.input,
              width:"200px",
              placeholder: '请输入${title}',
              defaultValue:'',
            },
          ]
        },
        url: {
          add: "/testOrderMain_jvxeTable/testOrderMainJvxeTable/add",
          edit: "/testOrderMain_jvxeTable/testOrderMainJvxeTable/edit",
          queryById: "/testOrderMain_jvxeTable/testOrderMainJvxeTable/queryById",
          testOrderProductJvxeTable: {
            list: '/testOrderMain_jvxeTable/testOrderMainJvxeTable/queryTestOrderProductJvxeTableByMainId'
          },
        }
      }
    },
    props: {
      //表单禁用
      disabled: {
        type: Boolean,
        default: false,
        required: false
      }
    },
    computed: {
      formDisabled(){
        return this.disabled
      },
    },
    created () {
    },
    methods: {
      addBefore(){
        this.testOrderProductJvxeTableTable.dataSource=[]
      },
      getAllTable() {
        let values = this.tableKeys.map(key => getRefPromise(this, key))
        return Promise.all(values)
      },
      /** 调用完edit()方法之后会自动调用此方法 */
      editAfter() {
        this.$nextTick(() => {
        })
        // 加载子表数据
        if (this.model.id) {
          let params = { id: this.model.id }
          this.requestSubTableData(this.url.testOrderProductJvxeTable.list, params, this.testOrderProductJvxeTableTable)
        }
      },
      //校验所有一对一子表表单
        validateSubForm(allValues){
            return new Promise((resolve,reject)=>{
              Promise.all([
              ]).then(() => {
                resolve(allValues)
              }).catch(e => {
                if (e.error === VALIDATE_FAILED) {
                  // 如果有未通过表单验证的子表，就自动跳转到它所在的tab
                  this.activeKey = e.index == null ? this.activeKey : this.refKeys[e.index]
                } else {
                  console.error(e)
                }
              })
            })
        },
      /** 整理成formData */
      classifyIntoFormData(allValues) {
        let main = Object.assign(this.model, allValues.formValue)
        return {
          ...main, // 展开
          testOrderProductJvxeTableList: allValues.tablesValue[0].tableData,
        }
      },
      validateError(msg){
        this.$message.error(msg)
      },

    }
  }
</script>

<style scoped>
</style>