export enum uploadWays {
  INPUT = '代码输入',
  UPLOAD = '文件上传'
}

export enum retrieveStates {
  RETRIEVING = 'retrieving',
  SUCCESS = 'success',
  FAILED = 'failed',
  GRAMMAR = 'grammar'
}

export enum repoClassifications {
  FULL = 'full',
  ANDROID = 'android',
  MICROSERVICE = 'microservice',
  UTIL = 'util'
}

export const repoClassificationIconMap = new Map([
  [repoClassifications.FULL, '/github.png'],
  [repoClassifications.ANDROID, '/android.png'],
  [repoClassifications.MICROSERVICE, '/microservices.png'],
  [repoClassifications.UTIL, '/util.png']
]);

export const retrieveStatesMap = new Map([
  [retrieveStates.RETRIEVING, '检索中'],
  [retrieveStates.SUCCESS, '检索完毕'],
  [retrieveStates.FAILED, '检索失败'],
  [retrieveStates.GRAMMAR, '语法错误']
]);

export const repoClassificationMap = new Map([
  [repoClassifications.FULL, '全量检索'],
  [repoClassifications.ANDROID, '安卓应用'],
  [repoClassifications.MICROSERVICE, '微服务应用'],
  [repoClassifications.UTIL, '工程工具']
]);
