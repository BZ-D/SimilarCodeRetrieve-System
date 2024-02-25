import {request} from '@/utils/axios'
import axios from "axios";

interface IPageParams {
  pageIndex: number
  pageSize: number
}

export function uploadFile(data: FormData) {
  return axios({
    url: '/api/code/uploadFile',
    method: 'post',
    data
  })
}

export function triggerRetrieve(data: {
  tag: string
  recordContent: string
  repoClassification: string
}) {
  return request({
    url: '/api/retrieveRecord/retrieve',
    type: 'post',
    data
  })
}

export function getRetrieveRecordsWithState(params: IPageParams) {
  return request({
    url: '/api/retrieveRecord/listRetrieveResultsByPage',
    type: 'get',
    params
  });
}

export async function getFileContentByOssUrl(url: string) {
  const response = await fetch(url);
  return await response.text();
}

export function getResultListByRecordId(id: number) {
  return request({
    url: '/api/retrieveRecord/listSimilarCodesByPage',
    type: 'get',
    params: {
      retrieveId: id,
      pageIndex: 1,
      pageSize: 200
    }
  });
}

export function getCodeContentByRepoIdAndCodeTag(params: {
  repoId: number
  codeTag: string
}) {
  return request({
    url: '/api/codeRepo/getCodeContent',
    type: 'get',
    params
  })
}
