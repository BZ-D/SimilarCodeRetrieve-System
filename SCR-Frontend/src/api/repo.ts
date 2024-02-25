import {request} from '@/utils/axios'

export function getCodeRepoList(params: {
  repoClassification: string
  pageIndex: number
  pageSize: number
}) {
  return request({
    url: '/api/codeRepo/listReposByPage',
    type: 'get',
    params
  })
}

export function getCodeRepoStatistics(repoId: number) {
  return request({
    url: '/api/codeRepo/getCodeRepoStatistics',
    type: 'get',
    params: {
      repoId
    }
  })
}
