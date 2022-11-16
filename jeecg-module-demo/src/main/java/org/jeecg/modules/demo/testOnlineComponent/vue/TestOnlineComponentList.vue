<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="10" :lg="11" :md="12" :sm="24">
            <a-form-item label="创建日期">
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择开始时间" class="query-group-cust" v-model="queryParam.createTime_begin"></j-date>
              <span class="query-group-split-cust"></span>
              <j-date :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" placeholder="请选择结束时间" class="query-group-cust" v-model="queryParam.createTime_end"></j-date>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="下拉框">
              <j-dict-select-tag placeholder="请选择下拉框" v-model="queryParam.dropSelect" dictCode="sex"/>
            </a-form-item>
          </a-col>
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
              <a-button type="primary" @click="searchQuery" icon="search">查询</a-button>
              <a-button type="primary" @click="searchReset" icon="reload" style="margin-left: 8px">重置</a-button>
              <a @click="handleToggleSearch" style="margin-left: 8px">
                {{ toggleSearchStatus ? '收起' : '展开' }}
                <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>
              </a>
            </span>
          </a-col>
        </a-row>
      </a-form>
    </div>
    <!-- 查询区域-END -->

    <!-- 操作按钮区域 -->
    <div class="table-operator">
      <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
      <a-button type="primary" icon="download" @click="handleExportXls('测试 Online 表单控件')">导出</a-button>
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
        <template slot="pcaSlot" slot-scope="text">
          <div>{{ getPcaText(text) }}</div>
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
          <a  @click="handleEdit(record)">编辑</a>

          <a-divider  type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm  title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <test-online-component-modal ref="modalForm" @ok="modalFormOk"></test-online-component-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import TestOnlineComponentModal from './modules/TestOnlineComponentModal'
  import { loadCategoryData } from '@/api/api'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'
  import Area from '@/components/_util/Area'

  export default {
    name: 'TestOnlineComponentList',
    mixins:[JeecgListMixin, mixinDevice, ],
    components: {
      TestOnlineComponentModal
    },
    data () {
      return {
        description: '测试 Online 表单控件管理页面',
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
            title:'文本框',
            align:"center",
            dataIndex: 'nomalText'
          },
          {
            title:'密码框',
            align:"center",
            dataIndex: 'password'
          },
          {
            title:'下拉框',
            align:"center",
            dataIndex: 'dropSelect_dictText'
          },
          {
            title:'下拉框',
            align:"center",
            dataIndex: 'dropSelectTable_dictText'
          },
          {
            title:'下拉框',
            align:"center",
            dataIndex: 'dropSelectMulti_dictText'
          },
          {
            title:'下拉框',
            align:"center",
            dataIndex: 'dropSelectMultiTable_dictText'
          },
          {
            title:'下拉搜索',
            align:"center",
            dataIndex: 'dropSelectSearch_dictText'
          },
          {
            title:'单选框',
            align:"center",
            dataIndex: 'radio_dictText'
          },
          {
            title:'多选框',
            align:"center",
            dataIndex: 'multiSelect_dictText'
          },
          {
            title:'开关',
            align:"center",
            dataIndex: 'testSwitch',
            customRender: (text) => (text ? filterMultiDictText(this.dictOptions['testSwitch'], text) : ''),
          },
          {
            title:'日期',
            align:"center",
            dataIndex: 'nomalDate',
            customRender:function (text) {
              return !text?"":(text.length>10?text.substr(0,10):text)
            }
          },
          {
            title:'日期时间',
            align:"center",
            dataIndex: 'completeDate'
          },
          {
            title:'时间',
            align:"center",
            dataIndex: 'nomalTime'
          },
          {
            title:'文件',
            align:"center",
            dataIndex: 'nomalFile',
            scopedSlots: {customRender: 'fileSlot'}
          },
          {
            title:'图片',
            align:"center",
            dataIndex: 'image',
            scopedSlots: {customRender: 'imgSlot'}
          },
          {
            title:'用户选择',
            align:"center",
            dataIndex: 'userSelect_dictText'
          },
          {
            title:'部门选择',
            align:"center",
            dataIndex: 'departSelect_dictText'
          },
          {
            title:'分类字典',
            align:"center",
            dataIndex: 'treesel',
            customRender: (text) => (text ? filterMultiDictText(this.dictOptions['treesel'], text) : '')
          },
          {
            title:'省市区',
            align:"center",
            dataIndex: 'area',
            scopedSlots: {customRender: 'pcaSlot'}
          },
          {
            title:'popup',
            align:"center",
            dataIndex: 'popup'
          },
          {
            title:'自定义树',
            align:"center",
            dataIndex: 'customTreesel_dictText'
          },
          {
            title:'省',
            align:"center",
            dataIndex: 'province'
          },
          {
            title:'市',
            align:"center",
            dataIndex: 'city'
          },
          {
            title:'县区',
            align:"center",
            dataIndex: 'area1'
          },
          {
            title:'多行文本',
            align:"center",
            dataIndex: 'multiLimeText'
          },
          {
            title:'富文本',
            align:"center",
            dataIndex: 'editor',
            scopedSlots: {customRender: 'htmlSlot'}
          },
          {
            title:'markdown',
            align:"center",
            dataIndex: 'markdown'
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
          list: "/testOnlineComponent/testOnlineComponent/list",
          delete: "/testOnlineComponent/testOnlineComponent/delete",
          deleteBatch: "/testOnlineComponent/testOnlineComponent/deleteBatch",
          exportXlsUrl: "/testOnlineComponent/testOnlineComponent/exportXls",
          importExcelUrl: "testOnlineComponent/testOnlineComponent/importExcel",
          
        },
        dictOptions:{},
        pcaData:'',
        superFieldList:[],
      }
    },
    created() {
      this.pcaData = new Area()
      this.$set(this.dictOptions, 'testSwitch', [{text:'是',value:'Y'},{text:'否',value:'N'}])
    this.getSuperFieldList();
    },
    computed: {
      importExcelUrl: function(){
        return `${window._CONFIG['domianURL']}/${this.url.importExcelUrl}`;
      },
    },
    methods: {
      getPcaText(code){
        return this.pcaData.getText(code);
      },
      initDictConfig(){
        loadCategoryData({code:''}).then((res) => {
          if (res.success) {
            this.$set(this.dictOptions, 'treesel', res.result)
          }
        })
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'datetime',value:'createTime',text:'创建日期'})
        fieldList.push({type:'string',value:'nomalText',text:'文本框',dictCode:''})
        fieldList.push({type:'string',value:'password',text:'密码框',dictCode:''})
        fieldList.push({type:'string',value:'dropSelect',text:'下拉框',dictCode:'sex'})
        fieldList.push({type:'string',value:'dropSelectTable',text:'下拉框',dictCode:"sys_role,role_name,role_code"})
        fieldList.push({type:'list_multi',value:'dropSelectMulti',text:'下拉框',dictTable:"", dictText:'', dictCode:'rule_conditions'})
        fieldList.push({type:'list_multi',value:'dropSelectMultiTable',text:'下拉框',dictTable:"sys_depart", dictText:'depart_name', dictCode:'org_code'})
        fieldList.push({type:'sel_search',value:'dropSelectSearch',text:'下拉搜索',dictTable:"sys_user", dictText:'realname', dictCode:'username'})
        fieldList.push({type:'string',value:'radio',text:'单选框',dictCode:'sex'})
        fieldList.push({type:'string',value:'multiSelect',text:'多选框',dictCode:'database_type'})
        fieldList.push({type:'switch',value:'testSwitch',text:'开关'})
        fieldList.push({type:'date',value:'nomalDate',text:'日期'})
        fieldList.push({type:'datetime',value:'completeDate',text:'日期时间'})
        fieldList.push({type:'string',value:'nomalTime',text:'时间',dictCode:''})
        fieldList.push({type:'Text',value:'nomalFile',text:'文件',dictCode:''})
        fieldList.push({type:'Text',value:'image',text:'图片',dictCode:''})
        fieldList.push({type:'sel_user',value:'userSelect',text:'用户选择'})
        fieldList.push({type:'sel_depart',value:'departSelect',text:'部门选择'})
        fieldList.push({type:'string',value:'treesel',text:'分类字典'})
        fieldList.push({type:'pca',value:'area',text:'省市区'})
        fieldList.push({type:'popup',value:'popup',text:'popup', popup:{code:'demo',field:'name',orgFields:'name',destFields:'popup'}})
        fieldList.push({type:'string',value:'customTreesel',text:'自定义树'})
        fieldList.push({type:'string',value:'province',text:'省'})
        fieldList.push({type:'string',value:'city',text:'市',dictCode:''})
        fieldList.push({type:'string',value:'area1',text:'县区',dictCode:''})
        fieldList.push({type:'Text',value:'multiLimeText',text:'多行文本',dictCode:''})
        fieldList.push({type:'string',value:'editor',text:'富文本',dictCode:''})
        fieldList.push({type:'string',value:'markdown',text:'markdown',dictCode:''})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>