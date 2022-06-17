import {axios as request} from '@/utils/request'

// 我的发起的流程
export function myProcessList(query) {
  return request({
    url: '/flowable/task/myProcess',
    method: 'get',
    params: query
  })
}


// 取消申请
export function deleteByDataId(dataId,deleteReason) {
  const data = {
    dataId:dataId,
    deleteReason:deleteReason
  }
  return request({
    url: '/flowable/instance/deleteByDataId',
    method: 'post',
    params: data
  })
}


// 部署流程实例
export function deployStart(deployId) {
  return request({
    url: '/flowable/process/startFlow/' + deployId,
    method: 'get',
  })
}

