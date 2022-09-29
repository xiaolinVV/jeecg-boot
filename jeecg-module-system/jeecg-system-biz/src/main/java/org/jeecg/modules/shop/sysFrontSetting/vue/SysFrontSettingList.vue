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
      <a-button type="primary" icon="download" @click="handleExportXls('商城设置')">导出</a-button>
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

    <sys-front-setting-modal ref="modalForm" @ok="modalFormOk"></sys-front-setting-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import SysFrontSettingModal from './modules/SysFrontSettingModal'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'SysFrontSettingList',
    mixins:[JeecgListMixin, mixinDevice],
    components: {
      SysFrontSettingModal
    },
    data () {
      return {
        description: '商城设置管理页面',
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
            title:'小程序名称',
            align:"center",
            dataIndex: 'name'
          },
          {
            title:'小程序描述',
            align:"center",
            dataIndex: 'description'
          },
          {
            title:'小程序logo',
            align:"center",
            dataIndex: 'frontLogo',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'首页推广图',
            align:"center",
            dataIndex: 'homeGeneralizeAddr',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'礼包胶囊图',
            align:"center",
            dataIndex: 'giftCapsuleAddr',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'商品数据来源标签(小程序端)',
            align:"center",
            dataIndex: 'goodLabelSmallsoft_dictText'
          },
          {
            title:'商品数据来源标签(app端)',
            align:"center",
            dataIndex: 'goodLabelApp_dictText'
          },
          {
            title:'首页底部推荐类型',
            align:"center",
            dataIndex: 'indexBottomRecommend_dictText'
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
          list: "/sysFrontSetting/sysFrontSetting/list",
          delete: "/sysFrontSetting/sysFrontSetting/delete",
          deleteBatch: "/sysFrontSetting/sysFrontSetting/deleteBatch",
          exportXlsUrl: "/sysFrontSetting/sysFrontSetting/exportXls",
          importExcelUrl: "sysFrontSetting/sysFrontSetting/importExcel",
          
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
        fieldList.push({type:'string',value:'name',text:'小程序名称',dictCode:''})
        fieldList.push({type:'string',value:'description',text:'小程序描述',dictCode:''})
        fieldList.push({type:'string',value:'frontLogo',text:'小程序logo',dictCode:''})
        fieldList.push({type:'string',value:'homeGeneralizeAddr',text:'首页推广图',dictCode:''})
        fieldList.push({type:'string',value:'giftCapsuleAddr',text:'礼包胶囊图',dictCode:''})
        fieldList.push({type:'string',value:'goodLabelSmallsoft',text:'商品数据来源标签(小程序端)',dictCode:'storeSourceOptions'})
        fieldList.push({type:'string',value:'goodLabelApp',text:'商品数据来源标签(app端)',dictCode:'storeSourceOptions'})
        fieldList.push({type:'string',value:'indexBottomRecommend',text:'首页底部推荐类型',dictCode:'indexBottomRecommend'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>