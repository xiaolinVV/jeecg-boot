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
      <a-button type="primary" icon="download" @click="handleExportXls('店铺设置')">导出</a-button>
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

    <store-setting-modal ref="modalForm" @ok="modalFormOk"></store-setting-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import StoreSettingModal from './modules/StoreSettingModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'StoreSettingList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      StoreSettingModal
    },
    data () {
      return {
        description: '店铺设置管理页面',
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
            title:'开店功能',
            align:"center",
            dataIndex: 'openStore_dictText'
          },
          {
            title:'店铺管理地址',
            align:"center",
            dataIndex: 'manageAddress'
          },
          {
            title:'店铺初始密码',
            align:"center",
            dataIndex: 'initialPasswd'
          },
          {
            title:'默认服务距离',
            align:"center",
            dataIndex: 'serviceDistance'
          },
          {
            title:'店铺首页推荐店铺',
            align:"center",
            dataIndex: 'storeCut_dictText'
          },
          {
            title:'店铺欢迎弹窗',
            align:"center",
            dataIndex: 'autoWelcome_dictText'
          },
          {
            title:'店铺列表默认排序',
            align:"center",
            dataIndex: 'defaultSort_dictText'
          },
          {
            title:'开店邀请图',
            align:"center",
            dataIndex: 'inviteFigure',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'分享图',
            align:"center",
            dataIndex: 'coverPlan',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'海报图',
            align:"center",
            dataIndex: 'posters',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'开店宣传图',
            align:"center",
            dataIndex: 'openPublicityPic',
            scopedSlots: {customRender: 'imgSlot'}
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
          list: "/storeSetting/storeSetting/list",
          delete: "/storeSetting/storeSetting/delete",
          deleteBatch: "/storeSetting/storeSetting/deleteBatch",
          exportXlsUrl: "/storeSetting/storeSetting/exportXls",
          importExcelUrl: "storeSetting/storeSetting/importExcel",
          
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
        fieldList.push({type:'string',value:'openStore',text:'开店功能',dictCode:'openStore'})
        fieldList.push({type:'string',value:'manageAddress',text:'店铺管理地址',dictCode:''})
        fieldList.push({type:'string',value:'initialPasswd',text:'店铺初始密码',dictCode:''})
        fieldList.push({type:'BigDecimal',value:'serviceDistance',text:'默认服务距离',dictCode:''})
        fieldList.push({type:'string',value:'storeCut',text:'店铺首页推荐店铺',dictCode:'storeCut'})
        fieldList.push({type:'string',value:'autoWelcome',text:'店铺欢迎弹窗',dictCode:'autoWelcome'})
        fieldList.push({type:'string',value:'defaultSort',text:'店铺列表默认排序',dictCode:'defaultSort'})
        fieldList.push({type:'string',value:'inviteFigure',text:'开店邀请图',dictCode:''})
        fieldList.push({type:'string',value:'coverPlan',text:'分享图',dictCode:''})
        fieldList.push({type:'string',value:'posters',text:'海报图',dictCode:''})
        fieldList.push({type:'string',value:'openPublicityPic',text:'开店宣传图',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>