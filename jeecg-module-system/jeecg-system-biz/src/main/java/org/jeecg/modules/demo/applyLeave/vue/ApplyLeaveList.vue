<template>
  <a-card :bordered="false">
    <!-- 查询区域 -->
    <div class="table-page-search-wrapper">
      <a-form layout="inline" @keyup.enter.native="searchQuery">
        <a-row :gutter="24">
          <a-col :xl="6" :lg="7" :md="8" :sm="24">
            <a-form-item label="审批状态">
              <j-dict-select-tag placeholder="请选择审批状态" v-model="queryParam.bpmStatus" dictCode="act_status"/>
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
      <a-button type="primary" icon="download" @click="handleExportXls('请假申请')">导出</a-button>
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
          <a  v-if='isEditOrDelete(record)'  @click="handleEdit(record)">编辑</a>

          <a-divider  v-if='isEditOrDelete(record)'  type="vertical" />
          <a-dropdown>
            <a class="ant-dropdown-link">更多 <a-icon type="down" /></a>
            <a-menu slot="overlay">
              <a-menu-item>
                <a @click="handleDetail(record)">详情</a>
              </a-menu-item>
              <a-menu-item  v-if='record.jimuReportId'>
                 <a @click="goToApplyDoc(record)">审批单据</a>
              </a-menu-item>
              <a-menu-item v-if="record.bpmStatus === '0'">
                <a @click="startProcess(record)">发起流程</a>
              </a-menu-item>
              <a-menu-item>
                <a-popconfirm  v-if='isEditOrDelete(record)'  title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                  <a>删除</a>
                </a-popconfirm>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>

      </a-table>
    </div>

    <apply-leave-modal ref="modalForm" @ok="modalFormOk"></apply-leave-modal>
  </a-card>
</template>

<script>

  import '@/assets/less/TableExpand.less'
  import { mixinDevice } from '@/utils/mixin'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import ApplyLeaveModal from './modules/ApplyLeaveModal'
  import { postAction } from '@/api/manage'
  import { FlowableMixin } from '@views/flowable/mixin/FlowableMixin'
  import Vue from 'vue'
  import { ACCESS_TOKEN } from '@/store/mutation-types'
  import {filterMultiDictText} from '@/components/dict/JDictSelectUtil'

  export default {
    name: 'ApplyLeaveList',
    mixins:[JeecgListMixin, mixinDevice ,FlowableMixin],
    components: {
      ApplyLeaveModal
    },
    data () {
      return {
        description: '请假申请管理页面',
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
            title:'请假天数',
            align:"center",
            dataIndex: 'leaveDays'
          },
          {
            title:'请假原因',
            align:"center",
            dataIndex: 'leaveReason'
          },
          {
            title:'审批状态',
            align:"center",
            dataIndex: 'bpmStatus_dictText'
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
          list: "/applyLeave/applyLeave/list",
          delete: "/applyLeave/applyLeave/delete",
          deleteBatch: "/applyLeave/applyLeave/deleteBatch",
          exportXlsUrl: "/applyLeave/applyLeave/exportXls",
          importExcelUrl: "applyLeave/applyLeave/importExcel",
          startProcess: '/flowable/definition/startByDataId/'
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
      startProcess(record){
        this.$confirm({
          title:'提示',
          content:'确认提交流程吗?',
          onOk:()=>{
            let params = Object.assign({dataId: record.id}, record);
            postAction(this.url.startProcess + record.id, params).then(res=>{
              if(res.success){
                this.$message.success(res.message);
                this.loadData();
                this.onClearSelected();
              }else{
                this.$message.warning(res.message);
              }
            }).catch((e)=>{
              this.$message.warning('不识别的请求!');
            })
          }
        })
      },
      goToApplyDoc(record) {
        let jimuReportId = record.jimuReportId
        let reportUrl = window._CONFIG['domianURL'] + '/jmreport/view/' + jimuReportId + '?token=' + Vue.ls.get(ACCESS_TOKEN) + '&id=' + record.id
        window.open(reportUrl)
      },
      initDictConfig(){
      },
      getSuperFieldList(){
        let fieldList=[];
        fieldList.push({type:'int',value:'leaveDays',text:'请假天数',dictCode:''})
        fieldList.push({type:'string',value:'leaveReason',text:'请假原因',dictCode:''})
        fieldList.push({type:'string',value:'bpmStatus',text:'审批状态',dictCode:'act_status'})
        this.superFieldList = fieldList
      }
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less';
</style>