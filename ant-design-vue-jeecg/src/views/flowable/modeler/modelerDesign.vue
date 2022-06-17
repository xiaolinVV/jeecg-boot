<template>
  <div>
<!--  ==================流程定义列表===============  -->
    <a-card v-if="!xmlFrame.open||xmlView">
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="68px">
      <el-form-item label="流程名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="流程分类" prop="category">
        <el-select @change="handleQuery" v-model="queryParams.category" placeholder="请选择流程分类" clearable prop="category">
          <el-option label="请选择" value="" />
          <el-option v-for="category in categorys" :key="category.id" :label="category.name" :value="category.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="激活" prop="active">
        <el-switch
          v-model="queryParams.active"
          active-color="#13ce66"
          inactive-color="#ff4949"
          @change="handleQuery"
        >
        </el-switch>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
      <el-form-item style="float:right">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleLoadXml"
        >新增流程定义</el-button>
      </el-form-item>
    </el-form>

      <el-table
        v-loading="loading" fit
        :data="definitionList"
        row-key="id"
        border
        lazy
        :load="load"
        :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column label="流程定义id" align="center" prop="id" />
      <el-table-column label="流程标识Key" align="center" prop="key"  />
      <el-table-column label="流程分类" align="center" >
        <template slot-scope="scope">
            <span>{{ getCategoryName(scope.row.category) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程名称" align="center" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-button type="text" @click="handleReadImage(scope.row.deploymentId)">
            <span>{{ scope.row.name }}</span>
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="流程版本" align="center">
        <template slot-scope="scope">
          <el-tag size="medium" >v{{ scope.row.version }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.suspensionState === 1">激活</el-tag>
          <el-tag type="warning" v-if="scope.row.suspensionState === 2">挂起</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="部署时间" align="center" prop="deploymentTime" width="180"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-dropdown>
            <span class="el-dropdown-link">
              更多操作<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item icon="el-icon-edit-outline" @click.native="handleLoadXml(scope.row)">
                编辑
              </el-dropdown-item>
<!--              <el-dropdown-item icon="el-icon-connection" @click.native="handleAddForm(scope.row)" v-if="scope.row.formId == null">
                配置表单
              </el-dropdown-item>-->
              <el-dropdown-item icon="el-icon-video-pause" @click.native="handleUpdateSuspensionState(scope.row)" v-if="scope.row.suspensionState === 1">
                挂起
              </el-dropdown-item>
              <el-dropdown-item icon="el-icon-video-play" @click.native="handleUpdateSuspensionState(scope.row)" v-if="scope.row.suspensionState === 2">
                激活
              </el-dropdown-item>
              <el-dropdown-item icon="el-icon-delete" @click.native="handleDelete(scope.row)" >
                删除
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      v-show="total>0"
      :total="total"
      :current-page.sync="queryParams.pageNum"
      :page-size.sync="queryParams.pageSize"
      @size-change="getList"
      @current-change="getList"
    />
    </a-card>
      <!-- 流程图 -->
    <a-card v-if="xmlFrame.open&&!xmlView" :title="xmlFrame.title">
      <a slot="extra" href="#" @click="()=>{xmlFrame.open=false}">返回</a>
      <bpmn-modeler
        v-if="xmlShow"
        ref="refNode"
        :xml="xmlData"
        :users="users"
        :groups="groups"
        :categorys="categorys"
        :is-view="xmlView"
        @save="save"
      />
    </a-card>
<!--  弹窗预览  -->
      <a-modal :title="xmlFrame.title" :visible.sync="xmlView&&xmlFrame.open" :width="xmlFrame.width"
        :footer="null" closable @cancel="()=>{xmlView=false,xmlFrame.open=false}"
      >
        <bpmn-modeler
          v-if="xmlShow"
          ref="refNode"
          :xml="xmlData"
          :users="users"
          :groups="groups"
          :categorys="categorys"
          :is-view="xmlView"
          @save="save"
        />
      </a-modal>

  </div>
</template>

<script>
import bpmnModeler from "workflow-bpmn-modeler";
import {
  categoryList,
  delDeployment,
  listDefinition,
  readXml,
  roleList,
  saveXml,
  updateState,
  userList
} from "@views/flowable/api/definition";

export default {
  components: {
    bpmnModeler,
  },
  data() {
    return {
      /*===================设计器属性======================*/
      users: [],
      groups: [],
      categorys: [],
      /*=================页面属性===================*/
      loading: true,
      // 总条数
      total: 0,
      // 流程定义表格数据
      definitionList: [],
      allDefinitionList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        category: null,
        key: null,
        tenantId: null,
        deployTime: null,
        derivedFrom: null,
        derivedFromRoot: null,
        parentDeploymentId: null,
        engineVersion: null
      },
      xmlFrame:{
        width:'70%',
        title:'流程图',
        open: false,
        src: "",
      },
      // xml
      xmlData:"",
      xmlShow: true,
      xmlView: false,

    };
  },
  created() {
    this.initUserAndRole();
    this.getList();
  },
  methods: {
    /*===============设计器===============*/
    initUserAndRole(){
      userList({}).then(res=>{
        this.users = res.result||[]
        this.users.map(o=>{
          o.id = o.username
          o.name = o.realname
        })
      })
      roleList({}).then(res=>{
        this.groups = res.result||[]
        this.groups.map(o=>{
          o.name = o.roleName
        })
      })
      categoryList({}).then(res=>{
        this.categorys = res.result||[]
      })
    },
    getModelDetail(deployId) {
      // 发送请求，获取xml
      readXml(deployId).then(res =>{
        this.xmlData = res.result;
      })
    },
    getCategoryName(category){
      let find = this.categorys.find(o=>o.id==category);
      if (find){
        return find.name
      }
      return ''
    },
    /*保存流程定义*/
    save(data) {
      console.log(data);  // { process: {...}, xml: '...', svg: '...' }
      const params = {
        name: data.process.name,
        category: data.process.category,
        xml: data.xml
      }
      saveXml(params).then(res => {
        this.$message.success(res.message)
        // 关闭当前标签页并返回上个页面
        this.getList()
        this.xmlFrame.open = false
      })
    },
    /*================页面===============*/
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.queryParams.suspensionState = this.queryParams.active?1:0;
      this.getList();
    },
    /** 查询流程定义列表 */
    getList() {
      this.loading = true;
      // 最新版本
      const param1 = Object.assign({
        isLastVersion:1,
      },this.queryParams)
      listDefinition(param1).then(response => {
        this.definitionList = response.result.records;
        this.total = response.result.total;
        this.loading = false;
        for (const definition of this.definitionList) {
          definition.hasChildren = true
        }
      });
      // 所有
      const param2 = Object.assign({
        isLastVersion:0
      },this.queryParams,{
        pageSize: 9999,
        pageNum:1
      })
      listDefinition(param2).then(response => {
        console.log(response)
        this.allDefinitionList = response.result.records;
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    resetForm(formName) {
      this.$refs[formName].resetFields()
    },
    /** 打开流程设计弹窗页面 */
    handleLoadXml(row){
      if (row&&row.deploymentId){
        console.log(row.deploymentId)
        this.handleReadImage(row.deploymentId)
        this.xmlView = false
        this.xmlFrame.title = "编辑流程图";
      } else {
        //新增
        this.xmlData = ''
        this.xmlView = false
        this.xmlFrame.open = true
        this.xmlFrame.title = '新增流程'
        this.xmlShow = false
        this.$nextTick(()=>{
          this.xmlShow = true
        })
      }
      this.xmlFrame.width = '90%'
    },
    /** 流程图查看 */
    handleReadImage(deploymentId){
      this.xmlFrame.title = "流程图";
      this.xmlFrame.open = true;
      this.xmlFrame.width = '70%';
      // this.xmlFrame.src = process.env.VUE_APP_BASE_API + "/flowable/definition/xmlFrame/" + deploymentId;
      // 发送请求，获取xml
      this.xmlView = true
      readXml(deploymentId).then(res =>{
        if (res.success){
          this.xmlData = res.result
          /*this.xmlShow = false
          this.$nextTick(()=>{
            this.xmlShow = true
          })*/
        } else {
          this.$message.error("获取流程图失败！")
        }
      })
    },
    // 打开业务表单
    handleForm() {
    },
    // 配置业务表单
    handleAddForm(row) {
    },
    /** 挂起/激活流程 */
    handleUpdateSuspensionState(row){
      let state = 1;
      if (row.suspensionState === 1) {
        state = 2
      }
      const params = {
        deployId: row.deploymentId,
        state: state
      }
      updateState(params).then(res => {
        this.$message.success(res.message);
        this.getList();
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      // const ids = row.deploymentId || this.ids;
      const params = {
        deployId: row.deploymentId
      }
      this.$confirm({
        title:"警告",
        content:'是否确认删除流程定义编号为"' + params.deployId + '"的数据项?',
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
        onOk:()=>{
          delDeployment(params).then(res=>{
            this.getList();
            if (res.success){
              this.$message.success('删除成功');
            } else {
              this.$message.success('删除失败');
            }
          })
        }
      })
    },
    load(tree, treeNode, resolve) {
      const key = tree.key;
      const childrens = []
      for (const one of this.allDefinitionList) {
        if (one.key==key&&one.id!=tree.id){
          childrens.push(one)
        }
      }
      console.log(tree, treeNode,this.allDefinitionList,childrens)
      resolve(childrens)
    }
  },
  computed: {
    getContainer() {
      return document.querySelector('#app')
    }
  }
};
</script>