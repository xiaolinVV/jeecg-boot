<style lang="less">
</style>
<template>
  <div class="search">
    <a-card>
      <p slot="title">
        <span>流程图</span>
      </p>
      <div :style="{height: svgHeight}" v-if="svgShow">
        <bpmnModeler class="svg" ref="bpm" :xml="xmlData" :is-view="true"></bpmnModeler>
      </div>
    </a-card>
    <a-card style="margin-top:10px;">
      <p slot="title">
        <span>流程审批进度历史</span>
      </p>
      <a-row style="position:relative">
        <div class="block">
          <a-timeline>
            <a-timeline-item
              v-for="(item,index ) in flowRecordList"
              :key="index"
              :color="setColor(item.finishTime)"
            >
              <p style="font-weight: 700;">{{item.taskName}}
                <i v-if="!item.finishTime" style="color: orange">(待办中。。。)</i>
              </p>

              <a-card :body-style="{ padding: '10px' }">
                <label v-if="item.assigneeName&&item.finishTime" style="font-weight: normal;margin-right: 30px;">实际办理人： {{item.assigneeName}} <a-tag type="info" size="mini">{{item.deptName}}</a-tag></label>
                <label v-if="item.candidate" style="font-weight: normal;margin-right: 30px;">候选办理人： {{item.candidate}}</label>
                <label style="font-weight: normal">接收时间： </label><label style="color:#8a909c;font-weight: normal">{{item.createTime}}</label>
                <label v-if="item.finishTime" style="margin-left: 30px;font-weight: normal">办结时间： </label><label style="color:#8a909c;font-weight: normal">{{item.finishTime}}</label>
                <label v-if="item.duration" style="margin-left: 30px;font-weight: normal">耗时： </label><label style="color:#8a909c;font-weight: normal">{{item.duration}}</label>

                <p  v-if="item.comment">
<!--  1 正常意见  2 退回意见 3 驳回意见                -->
                  <a-tag color="green" v-if="item.comment.type === '1'">
                    <span v-if="item.comment.comment!='重新提交'">通过：</span>
                    {{item.comment.comment}}
                  </a-tag>
                  <a-tag color="orange" v-if="item.comment.type === '2'">退回：  {{item.comment.comment}}</a-tag>
                  <a-tag color="red" v-if="item.comment.type === '3'">驳回：  {{item.comment.comment}}</a-tag>
                </p>
              </a-card>
            </a-timeline-item>
          </a-timeline>
        </div>
      </a-row>
    </a-card>
  </div>
</template>

<script>
import {flowRecord} from "@views/flowable/api/finished";
import {getFlowViewerByDataId, readXmlByDataId} from "@views/flowable/api/definition";
import bpmnModeler from "workflow-bpmn-modeler";

export default {
    name: 'HistoricDetail',
  components: {
    bpmnModeler,
  },
    props: {
    /**/
        dataId: {
            type: String,
            default: '',
            required: true
        },

    },
    data() {
        return {
            taskList:[],
            flowRecordList: [], // 流程流转数据
            formData:{},
            xmlData:'',
            type: 0,
            loading: false, // 表单加载状态
            loadingImg: false,
            data: [],
            id: '',
            imgUrl: '',
            backRoute: '',
          svgHeight:'',
          svgShow: true
        };
    },
    created() {
        this.init();
    },
    watch: {
      dataId: function(newval, oldName) {
            this.init();
        }
    },

    methods: {
        init() {
          this.getFlowRecordList()
          this.getModelDetail()
        },
      /** xml 文件 */
      getModelDetail() {
        // 发送请求，获取xml
        readXmlByDataId(this.dataId).then(res => {
          this.xmlData = res.result
          this.getFlowViewer()
          setTimeout(()=>{
            this.fitViewport()
          })
        })
      },
      // 流程进行情况
      getFlowViewer() {
        getFlowViewerByDataId(this.dataId).then(res => {
          this.taskList = res.result || []
          this.fillColor();
        })
      },
      /** 流程流转记录 */
      getFlowRecordList() {
        const params = {dataId: this.dataId}
        flowRecord(params).then(res => {
          // console.log(res)
          this.flowRecordList = res.result.flowList;
          this.finishOrder()
          // 流程过程中不存在初始化表单 直接读取的流程变量中存储的表单值
          if (res.result.formData) {
            this.formData = res.result.formData;
          }
        }).catch(res => {
          console.log(res)
        })
      },
        //整理顺序，把待办放最上面，并且只留一个（不然会签时会乱）
      finishOrder(){
        const list = []
        let noFinish = null
        for (const flow of this.flowRecordList) {
          if (flow.finishTime){
            // 办结的节点同时取有实际办理人的，因为会签会将所有的多实例都返回，需要过滤
            if (flow.assigneeId){
              list.push(flow)
            }
          } else {
            noFinish = flow
          }
        }
        if (noFinish){
          const find = list.find(obj=>obj.taskDefKey == noFinish.taskDefKey);
          if (find){
            noFinish.taskName = '【会签中】'+noFinish.taskName
          }
          this.flowRecordList = [noFinish,...list];
        } else {
          this.flowRecordList = list;
        }

      },
        setColor(val) {
          if (val) {
            return "#2bc418";
          } else {
            return "#b3bdbb";
          }
        },
      fillColor() {
        const modeler = this.$refs.bpm.modeler;
        const canvas = modeler.get('canvas')
        modeler._definitions.rootElements[0].flowElements.forEach(n => {
          const completeTask = this.taskList.find(m => m.key === n.id)
          const todoTask = this.taskList.find(m => !m.completed)
          const endTask = this.taskList[this.taskList.length - 1]
          //用户任务
          if (n.$type === 'bpmn:UserTask') {
            if (completeTask) {
              canvas.addMarker(n.id, completeTask.completed ? 'highlight' : 'highlight-todo')
              canvas.addMarker(n.id, completeTask.back ? 'highlight-back' : 'highlight-noback')
              n.outgoing.forEach(nn => {
                const targetTask = this.taskList.find(m => m.key === nn.targetRef.id)
                if (targetTask) {
                  if (todoTask && completeTask.key === todoTask.key && !todoTask.completed){
                    canvas.addMarker(nn.id, todoTask.completed ? 'highlight' : 'highlight-todo')
                    canvas.addMarker(nn.targetRef.id, todoTask.completed ? 'highlight' : 'highlight-todo')
                  }else {
                    canvas.addMarker(nn.id, targetTask.completed ? 'highlight' : 'highlight-todo')
                    canvas.addMarker(nn.targetRef.id, targetTask.completed ? 'highlight' : 'highlight-todo')
                  }
                }
              })
            }
          }
          // 排他网关
          else if (n.$type === 'bpmn:ExclusiveGateway') {
            if (completeTask) {
              canvas.addMarker(n.id, completeTask.completed ? 'highlight' : 'highlight-todo')
              n.outgoing.forEach(nn => {
                const targetTask = this.taskList.find(m => m.key === nn.targetRef.id)
                if (targetTask) {

                  canvas.addMarker(nn.id, targetTask.completed ? 'highlight' : 'highlight-todo')
                  canvas.addMarker(nn.targetRef.id, targetTask.completed ? 'highlight' : 'highlight-todo')
                }

              })
            }

          }
          // 并行网关
          else if (n.$type === 'bpmn:ParallelGateway') {
            if (completeTask) {
              canvas.addMarker(n.id, completeTask.completed ? 'highlight' : 'highlight-todo')
              n.outgoing.forEach(nn => {
                debugger
                const targetTask = this.taskList.find(m => m.key === nn.targetRef.id)
                if (targetTask) {
                  canvas.addMarker(nn.id, targetTask.completed ? 'highlight' : 'highlight-todo')
                  canvas.addMarker(nn.targetRef.id, targetTask.completed ? 'highlight' : 'highlight-todo')
                }
              })
            }
          }
          else if (n.$type === 'bpmn:StartEvent') {
            n.outgoing.forEach(nn => {
              const completeTask = this.taskList.find(m => m.key === nn.targetRef.id)
              if (completeTask) {
                canvas.addMarker(nn.id, 'highlight')
                canvas.addMarker(n.id, 'highlight')
                return
              }
            })
          }
          else if (n.$type === 'bpmn:EndEvent') {
            if (endTask.key === n.id && endTask.completed) {
              canvas.addMarker(n.id, 'highlight')
              return
            }
          }
        })
      },
      // 让图能自适应屏幕
      fitViewport() {
        const modeler = this.$refs.bpm.modeler;
        const canvas = modeler.get('canvas')
        // this.zoom = this.modeler.get('canvas').zoom('fit-viewport')
        if (this.svgHeight){
          document.querySelector('.canvas').style.height = this.svgHeight;
        }
        const bbox = document.querySelector('.flow-containers .viewport').getBBox()
        const currentViewbox = modeler.get('canvas').viewbox()
        if (!this.svgHeight){
          this.svgHeight = currentViewbox.inner.height + 'px'
          this.svgShow = false
          this.$nextTick(()=>{
            this.svgShow = true
          })
          // this.fitViewport()
          setTimeout(()=>{
            this.fitViewport()
          })
        }

        const elementMid = {
          x: bbox.x + bbox.width / 2 - 65,
          y: bbox.y + bbox.height / 2
        }
        // 调节位置
        modeler.get('canvas').viewbox({
          x: elementMid.x - currentViewbox.width / 2 + 70,
          y: elementMid.y - currentViewbox.height/2,
          width: currentViewbox.width,
          height: currentViewbox.height
        })
        // 调节大小缩放
        const zoom = currentViewbox.outer.width /(currentViewbox.inner.width+200)
        console.log('********',zoom,elementMid,currentViewbox.inner,currentViewbox.outer)
        // modeler.get('canvas').zoom(zoom)


      },
    }

};
</script>
<style lang="less">
   .highlight.djs-shape .djs-visual > :nth-child(1) {
     fill: green !important;
     stroke: green !important;
     fill-opacity: 0.2 !important;
   }
   .highlight.djs-shape .djs-visual > :nth-child(2) {
     fill: green !important;
   }
   .highlight.djs-shape .djs-visual > path {
     fill: green !important;
     fill-opacity: 0.2 !important;
     stroke: green !important;
   }
   .highlight.djs-connection > .djs-visual > path {
     stroke: green !important;
   }
   // .djs-connection > .djs-visual > path {
   //   stroke: orange !important;
   //   stroke-dasharray: 4px !important;
   //   fill-opacity: 0.2 !important;
   // }
   // .djs-shape .djs-visual > :nth-child(1) {
   //   fill: orange !important;
   //   stroke: orange !important;
   //   stroke-dasharray: 4px !important;
   //   fill-opacity: 0.2 !important;
   // }
   .highlight-todo.djs-connection > .djs-visual > path {
     stroke: orange !important;
     stroke-dasharray: 4px !important;
     fill-opacity: 0.2 !important;
   }
   .highlight-todo.djs-shape .djs-visual > :nth-child(1) {
     fill: orange !important;
     stroke: orange !important;
     stroke-dasharray: 4px !important;
     fill-opacity: 0.2 !important;
   }
   .highlight-back.djs-connection > .djs-visual > path {
     stroke: red !important;
     stroke-dasharray: 4px !important;
     fill-opacity: 0.2 !important;
   }
   .highlight-back.djs-shape .djs-visual > :nth-child(1) {
     fill: red !important;
     stroke: red !important;
     stroke-dasharray: 4px !important;
     fill-opacity: 0.2 !important;
   }
   .overlays-div {
     font-size: 10px;
     color: red;
     width: 100px;
     top: -20px !important;
   }
</style>