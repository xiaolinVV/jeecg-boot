<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('order_refund_list')">导出</a-button>
      <a-upload name="file" :showUploadList="false" :multiple="false" :headers="tokenHeader" :action="importExcelUrl" @change="handleImportExcel">
        <a-button type="primary" icon="import">导入</a-button>
      </a-upload>
      <!-- 高级查询区域 -->
      <j-super-query :fieldList="superFieldList" ref="superQueryModal" @handleSuperQuery="handleSuperQuery"></j-super-query>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-menu slot="overlay">
          <a-menu-item key="1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
        </a-menu>
        <a-button style="margin-left: 8px"> 批量操作 <a-icon type="down" /></a-button>
      </a-dropdown>
    </div>

    <!-- table区域-begin -->
    <div>
      <div class="ant-alert ant-alert-info" style="margin-bottom: 16px;">
        <i class="anticon anticon-info-circle ant-alert-icon"></i> 已选择 <a style="font-weight: 600">{{ selectedRowKeys.length }}</a>项
        <a style="margin-left: 24px" @click="onClearSelected">清空</a>
      </div>

      <a-table
        ref="table"
        size="middle"
        :scroll="{x:true}"
        bordered
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :pagination="ipagination"
        :loading="loading"
        :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
        class="j-table-force-nowrap"
        @change="handleTableChange">

        <template slot="htmlSlot" slot-scope="text">
          <div v-html="text"></div>
        </template>
        <template slot="imgSlot" slot-scope="text,record">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无图片</span>
          <img v-else :src="getImgView(text)" :preview="record.id" height="25px" alt="" style="max-width:80px;font-size: 12px;font-style: italic;"/>
        </template>
        <template slot="fileSlot" slot-scope="text">
          <span v-if="!text" style="font-size: 12px;font-style: italic;">无文件</span>
          <a-button
            v-else
            :ghost="true"
            type="primary"
            icon="download"
            size="small"
            @click="downloadFile(text)">
            下载
          </a-button>
        </template>

        <span slot="action" slot-scope="text, record">
          <a @click="handleEdit(record)">编辑</a>

          <a-divider type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <order-refund-list-modal ref="modalForm" @ok="modalFormOk"></order-refund-list-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import OrderRefundListModal from './modules/OrderRefundListModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'OrderRefundListList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      OrderRefundListModal
    },
    data () {
      return {
        description: 'order_refund_list管理页面',
        // 表头
        columns: [
          {
            title: '#',
            dataIndex: '',
            key:'rowIndex',
            width:60,
            align:"center",
            customRender:function (t,r,index) {
              return parseInt(index)+1;
            }
          },
          {
            title:'订单id',
            align:"center",
            dataIndex: 'orderListId'
          },
          {
            title:'订单号',
            align:"center",
            dataIndex: 'orderNo'
          },
          {
            title:'订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；',
            align:"center",
            dataIndex: 'orderType'
          },
          {
            title:'订单商品记录id',
            align:"center",
            dataIndex: 'orderGoodRecordId'
          },
          {
            title:'会员id',
            align:"center",
            dataIndex: 'memberId'
          },
          {
            title:'店铺用户id',
            align:"center",
            dataIndex: 'sysUserId'
          },
          {
            title:'退款类型 0=仅退款 1=退货退款 2=换货',
            align:"center",
            dataIndex: 'refundType'
          },
          {
            title:'退款原因',
            align:"center",
            dataIndex: 'refundReason'
          },
          {
            title:'拒绝退款理由',
            align:"center",
            dataIndex: 'refundExplain'
          },
          {
            title:'申请说明',
            align:"center",
            dataIndex: 'remarks'
          },
          {
            title:'售后状态 0=待处理 1=待买家退回 2=换货中 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成',
            align:"center",
            dataIndex: 'status'
          },
          {
            title:'商品主图相对地址（以json的形式存储多张）',
            align:"center",
            dataIndex: 'goodMainPicture'
          },
          {
            title:'平台商品id（只做对象映射）',
            align:"center",
            dataIndex: 'goodListId'
          },
          {
            title:'商品规格id（只做对象映射）',
            align:"center",
            dataIndex: 'goodSpecificationId'
          },
          {
            title:'商品名称',
            align:"center",
            dataIndex: 'goodName'
          },
          {
            title:'规格名称，按照顺序逗号隔开',
            align:"center",
            dataIndex: 'goodSpecification'
          },
          {
            title:'退款凭证图片，按照顺序逗号隔开',
            align:"center",
            dataIndex: 'refundCertificate'
          },
          {
            title:'退款金额',
            align:"center",
            dataIndex: 'refundPrice'
          },
          {
            title:'退款数量',
            align:"center",
            dataIndex: 'refundAmount'
          },
          {
            title:'物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；',
            align:"center",
            dataIndex: 'logisticsCompany'
          },
          {
            title:'快递单号',
            align:"center",
            dataIndex: 'trackingNumber'
          },
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            fixed:"right",
            width:147,
            scopedSlots: { customRender: 'action' }
          }
        ],
        url: {
          list: "/order/orderRefundList/list",
          delete: "/order/orderRefundList/delete",
          deleteBatch: "/order/orderRefundList/deleteBatch",
          exportXlsUrl: "/order/orderRefundList/exportXls",
          importExcelUrl: "order/orderRefundList/importExcel",
          
        },
        dictOptions:{},
        superFieldList:[],
      }
    },
    created() {
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'string',value:'orderListId',text:'订单id',dictCode:''})
        fieldList.push({type:'string',value:'orderNo',text:'订单号',dictCode:''})
        fieldList.push({type:'string',value:'orderType',text:'订单类型；0：普通订单；1：拼团订单；2：抢购订单；3：自选订单；4：兑换订单；数据字典：order_type；',dictCode:'order_type'})
        fieldList.push({type:'string',value:'orderGoodRecordId',text:'订单商品记录id',dictCode:''})
        fieldList.push({type:'string',value:'memberId',text:'会员id',dictCode:''})
        fieldList.push({type:'string',value:'sysUserId',text:'店铺用户id',dictCode:''})
        fieldList.push({type:'string',value:'refundType',text:'退款类型 0=仅退款 1=退货退款 2=换货',dictCode:'refund_type'})
        fieldList.push({type:'string',value:'refundReason',text:'退款原因',dictCode:'order_refund_reason'})
        fieldList.push({type:'string',value:'refundExplain',text:'拒绝退款理由',dictCode:''})
        fieldList.push({type:'string',value:'remarks',text:'申请说明',dictCode:''})
        fieldList.push({type:'string',value:'status',text:'售后状态 0=待处理 1=待买家退回 2=换货中 3=退款中 4=退款成功 5=已拒绝 6=退款关闭 7=换货关闭 8=换货完成',dictCode:'refund_status'})
        fieldList.push({type:'string',value:'goodMainPicture',text:'商品主图相对地址（以json的形式存储多张）',dictCode:''})
        fieldList.push({type:'string',value:'goodListId',text:'平台商品id（只做对象映射）',dictCode:''})
        fieldList.push({type:'string',value:'goodSpecificationId',text:'商品规格id（只做对象映射）',dictCode:''})
        fieldList.push({type:'string',value:'goodName',text:'商品名称',dictCode:''})
        fieldList.push({type:'string',value:'goodSpecification',text:'规格名称，按照顺序逗号隔开',dictCode:''})
        fieldList.push({type:'string',value:'refundCertificate',text:'退款凭证图片，按照顺序逗号隔开',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'refundPrice',text:'退款金额',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'refundAmount',text:'退款数量',dictCode:''})
        fieldList.push({type:'string',value:'logisticsCompany',text:'物流公司；0：顺丰速运；1：圆通快递；2：申通快递；3：中通快递；4：韵达快递；5：天天快递；6：中国邮政；7：EMS邮政特快专递；8：德邦快递；对应数据字典：logistics_company；',dictCode:'logistics_company'})
        fieldList.push({type:'string',value:'trackingNumber',text:'快递单号',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>