<template>
  <a-spin :spinning="confirmLoading">
    <j-form-container :disabled="formDisabled">
      <a-form-model ref="form" :model="model" :rules="validatorRules" slot="detail">
        <a-row>
          <a-col :span="6">
            <a-form-model-item label="文本框" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nomalText">
              <a-input v-model="model.nomalText" placeholder="请输入文本框"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="密码框" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="password">
              <a-input-password v-model="model.password" placeholder="请输入密码框" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="下拉框" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dropSelect">
              <j-dict-select-tag type="list" v-model="model.dropSelect" dictCode="sex" placeholder="请选择下拉框" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="下拉框" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dropSelectTable">
              <j-dict-select-tag type="list" v-model="model.dropSelectTable" dictCode="sys_role,role_name,role_code" placeholder="请选择下拉框" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="下拉框" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dropSelectMulti">
              <j-multi-select-tag type="list_multi" v-model="model.dropSelectMulti" dictCode="rule_conditions" placeholder="请选择下拉框" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="下拉框" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dropSelectMultiTable">
              <j-multi-select-tag type="list_multi" v-model="model.dropSelectMultiTable" dictCode="sys_depart,depart_name,org_code" placeholder="请选择下拉框" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="下拉搜索" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="dropSelectSearch">
              <j-search-select-tag v-model="model.dropSelectSearch" dict="sys_user,realname,username"  />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="单选框" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="radio">
              <j-dict-select-tag type="radio" v-model="model.radio" dictCode="sex" placeholder="请选择单选框" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="多选框" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="multiSelect">
              <j-multi-select-tag type="checkbox" v-model="model.multiSelect" dictCode="database_type" placeholder="请选择多选框" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="开关" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="testSwitch">
              <j-switch v-model="model.testSwitch"  ></j-switch>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="日期" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nomalDate">
              <j-date placeholder="请选择日期" v-model="model.nomalDate"  style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="日期时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="completeDate">
              <j-date placeholder="请选择日期时间"  v-model="model.completeDate" :show-time="true" date-format="YYYY-MM-DD HH:mm:ss" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="时间" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nomalTime">
              <j-time placeholder="请选择时间"  v-model="model.nomalTime" style="width: 100%" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="文件" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="nomalFile">
              <j-upload v-model="model.nomalFile"   ></j-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="图片" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="image">
              <j-image-upload isMultiple  v-model="model.image" ></j-image-upload>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="用户选择" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="userSelect">
              <j-select-user-by-dep v-model="model.userSelect" :multi="true" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="部门选择" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="departSelect">
              <j-select-depart v-model="model.departSelect" :multi="true"  />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="分类字典" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="treesel">
              <j-category-select v-model="model.treesel" pcode="" placeholder="请选择分类字典"  />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="省市区" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="area">
             <j-area-linkage type="cascader" v-model="model.area" placeholder="请输入省市区"  />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="popup" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="popup">
              <j-popup
                v-model="model.popup"
                field="popup"
                org-fields="name"
                dest-fields="popup"
                code="demo"
                :multi="true"
                @input="popupCallback"
                />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="自定义树" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="customTreesel">
  	          <j-tree-select
                ref="treeSelect"
                placeholder="请选择自定义树"
                v-model="model.customTreesel"
                dict="sys_category,name,id"
                pidValue="0"
                >
              </j-tree-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="省" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="province">
              <a-input v-model="model.province" placeholder="请输入省"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="市" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="city">
              <a-input v-model="model.city" placeholder="请输入市"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="县区" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="area1">
              <a-input v-model="model.area1" placeholder="请输入县区"  ></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="多行文本" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="multiLimeText">
              <a-textarea v-model="model.multiLimeText" rows="4" placeholder="请输入多行文本" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="富文本" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="editor">
              <j-editor v-model="model.editor" />
            </a-form-model-item>
          </a-col>
          <a-col :span="6">
            <a-form-model-item label="markdown" :labelCol="labelCol" :wrapperCol="wrapperCol" prop="markdown">
              <j-markdown-editor v-model="model.markdown" id="markdown"></j-markdown-editor>
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
    name: 'TestOnlineComponentForm',
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
          add: "/testOnlineComponent/testOnlineComponent/add",
          edit: "/testOnlineComponent/testOnlineComponent/edit",
          queryById: "/testOnlineComponent/testOnlineComponent/queryById"
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
      popupCallback(value,row){
         this.model = Object.assign(this.model, row);
      },
      handleCategoryChange(value,backObj){
         this.model = Object.assign(this.model, backObj);
      }
    }
  }
</script>