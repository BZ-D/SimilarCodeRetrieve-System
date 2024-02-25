<script setup lang="ts">
import CodeBlock from "@/components/CodeBlock.vue";
import {onMounted, ref} from "vue";
import type {UploadInstance, UploadProps, UploadUserFile} from "element-plus";
import {ElMessage} from "element-plus";
import LoadingRect from "@/components/LoadingRect.vue";
import {getFileContentByOssUrl, getRetrieveRecordsWithState, triggerRetrieve, uploadFile} from "@/api/retrieve";
import {repoClassifications, retrieveStates, uploadWays} from "@/common/retrieve";
import {useRouter} from "vue-router";
import {saveRecord} from "@/utils/record";
import {isValidUrl} from "@/utils/status";
import {repoClassificationIconMap, retrieveStatesMap, repoClassificationMap} from "@/common/retrieve";

export interface IRetrieveRecord {
  id: number
  tag: string
  recordContent: string
  createTime: string
  retrieveState: string
  repoClassification: string
}

const router = useRouter();

const uploadCodeWay = ref<string>(uploadWays.INPUT);
const repoClassification = ref<string>(repoClassifications.FULL); // 检索领域，全量或细分领域
const uploadCodeTag = ref<string>('');
const codeFileContentDialogVisible = ref<boolean>(false);
const uploading = ref<boolean>(false); // 点击检索按钮的 loading

const upload = ref<UploadInstance>()

// CodeMirror的ref
const codeEditor = ref<any>(null);

// 存放上传的文件
const codeFileList = ref<UploadUserFile[]>([]);

// 存放上传文件的内容
const codeFileContent = ref<string>('');

// 查询检索记录分页
const curPage = ref<number>(1);
const pageSize = ref<number>(15);
const total = ref<number>(0);

// 查看检索记录的代码内容
const retrieveRecordDialogVisible = ref<boolean>(false);
const chosenRecord = ref<IRetrieveRecord>(null);

// 存放检索记录
const retrieveRecords = ref<IRetrieveRecord[]>([]);
const retrieveRecordsTableLoading = ref<boolean>(false);

onMounted(() => {
  getRetrieveRecords();
})

// 刷新检索记录表格
const getRetrieveRecords = () => {
  retrieveRecordsTableLoading.value = true;
  getRetrieveRecordsWithState({
    pageIndex: curPage.value,
    pageSize: pageSize.value
  }).then((res) => {
    retrieveRecords.value = [];
    res.records.forEach((record) => retrieveRecords.value.push(record));
    total.value = res.total;
  }).finally(() => {
    retrieveRecordsTableLoading.value = false;
  })
}

// 点击检索记录列表中某一文件以查看其内容
const handleCheckRecordContent = async (id: number) => {
  const record = retrieveRecords.value.find((record) => record.id === id);
  let content: string;

  // 第一种情况：oss文件，recordContent为url，tag为文件名
  if (isValidUrl(record.recordContent)) {
    const loadingMsg = ElMessage.info({
      message: '加载 OBS 文件内容中……',
      showClose: true,
      duration: 0
    });
    content = await getFileContentByOssUrl(record.recordContent);
    loadingMsg.close();
  } else {
    content = record.recordContent;
  }

  chosenRecord.value = Object.assign({}, record);
  chosenRecord.value.recordContent = content;
  retrieveRecordDialogVisible.value = true;
};

// 读取选取的本地java文件内容
const readFileContent = (file: File) => {
  const reader = new FileReader();
  reader.onload = (event) => {
    // 文件读取成功的回调
    if (typeof event.target.result === "string") {
      codeFileContent.value = event.target.result;
    }
  };
  reader.readAsText(file);
}

// 用于处理上传的本地代码文件
const handleChange: UploadProps['onChange'] = (uploadFile, uploadFiles) => {
  let fileSize = Number(uploadFile.size / 1024 / 1024);
  if (fileSize > 5) {
    ElMessage.warning('上传的 Java 文件大小不能超过 5MB，请重新上传。');
    return;
  }
  codeFileList.value[0] = uploadFile;
  readFileContent(uploadFile.raw)
}

const emitText = (text: string) => {
  return text.length <= 25 ? text : text.substring(0, 20) + '···.java';
}

const clearInput = () => {
  codeFileList.value = [];
  codeFileContent.value = '';
  codeEditor.value.codeVal = '';
}

// 点击检索按钮
const handleRetrieve = async () => {
  if (uploadCodeWay.value === uploadWays.UPLOAD) {
    if (codeFileList.value.length === 0) {
      ElMessage.error('请选择本地代码文件后再上传检索');
      return;
    }
  }

  if (uploadCodeWay.value === uploadWays.INPUT) {
    if (uploadCodeTag.value === '') {
      ElMessage.error('请输入代码块标识 Tag');
      return;
    }
    if (codeEditor.value.codeVal === '') {
      ElMessage.error('请输入代码后再检索');
      return;
    }
  }

  const data = {
    tag: '',
    recordContent: '',
    repoClassification: repoClassification.value
  };

  if (uploadCodeWay.value === uploadWays.INPUT) {
    data.tag = uploadCodeTag.value;
    data.recordContent = codeEditor.value.codeVal;
  } else {
    uploading.value = true;
    const formData = new FormData();
    formData.append('codeFile', codeFileList.value[0].raw);
    const url = await uploadFile(formData);

    data.tag = codeFileList.value[0].name;
    data.recordContent = url;
  }

  triggerRetrieve(data)
    .then(() => {
      ElMessage.success('检索请求成功');
      getRetrieveRecords();
      clearInput();
    })
    .finally(() => {
      uploading.value = false;
    })
}

// 查看检索结果
const handleCheckResult = (recordId: number) => {
  saveRecord(recordId, retrieveRecords.value.find((record) => record.id === recordId) || {});
  router.push({
    path: '/home/retrieve/result',
    query: {
      recordId
    }
  });
}

const handleReretrieve = () => {
  ElMessage.success('重新检索请求成功');
  getRetrieveRecords();
}
</script>

<template>
  <el-row class="retrieve-container" :gutter="20">
    <el-col class="code-area" :span="9">
      <div class="upload-code-way">
        <el-radio-group v-model="uploadCodeWay">
          <el-radio-button :label="uploadWays.INPUT"/>
          <el-radio-button :label="uploadWays.UPLOAD"/>
          >
        </el-radio-group>
        <div class="repo-classification-choose-area">
          <div class="label">
            检索领域：
          </div>
          <el-select v-model="repoClassification">
            <el-option :label="repoClassificationMap.get(repoClassifications.FULL)" :value="repoClassifications.FULL"/>
            <el-option :label="repoClassificationMap.get(repoClassifications.ANDROID)"
                       :value="repoClassifications.ANDROID"/>
            <el-option :label="repoClassificationMap.get(repoClassifications.MICROSERVICE)"
                       :value="repoClassifications.MICROSERVICE"/>
            <el-option :label="repoClassificationMap.get(repoClassifications.UTIL)" :value="repoClassifications.UTIL"/>
          </el-select>
        </div>
      </div>
      <div v-show="uploadCodeWay === uploadWays.INPUT" class="code-input-area">
        <div class="code-tag-input-area">
          <span class="input-label">
            代码块 Tag：
          </span>
          <el-input
            v-model="uploadCodeTag"
            placeholder="代码块 Tag"
            style="display: inline; width: 300px;"
          />
        </div>
        <CodeBlock ref="codeEditor" font-size="12px"/>
      </div>
      <div v-show="uploadCodeWay === uploadWays.UPLOAD" class="code-upload-area">
        <el-upload
          ref="upload"
          drag
          :auto-upload="false"
          :on-change="handleChange"
          accept=".java"
          :show-file-list="false"
        >
          <el-icon class="el-icon--upload">
            <upload-filled/>
          </el-icon>
          <div class="el-upload__text">
            将要检索的 Java 文件拖拽到此处<br>
            或 <em>点击选择文件上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              支持上传 Java 文件，大小不超过 5 MB
            </div>
          </template>
        </el-upload>
        <div v-if="codeFileList.length > 0" class="uploaded-file-list">
          <div v-for="(file) in codeFileList" :key="file.uid" class="uploaded-file-item">
            <span>
              {{ emitText(file.name) || 'Uploaded.java' }}
            </span>
            <div class="code-file-operation-area">
              <el-link
                :underline="false"
                type="danger"
                @click="() => {
                  codeFileList = []
                }"
                style="margin-right: 10px;"
              >
                删除该文件
              </el-link>
              <el-link
                :underline="false"
                @click="() => {
                codeFileContentDialogVisible = true;
              }"
              >
                查看代码内容
              </el-link>
            </div>
          </div>
        </div>
      </div>
    </el-col>
    <el-col class="btn-area" :span="3">
      <el-button
        type="primary"
        :loading="uploading"
        loading-icon="stopwatch"
        :icon="uploadCodeWay === uploadWays.INPUT ? 'Position' : 'UploadFilled'"
        @click="handleRetrieve"
      >
        <span v-show="uploadCodeWay === uploadWays.UPLOAD">
          上传
        </span>检索
      </el-button>
    </el-col>
    <el-col class="list-area" :span="12">
      <div class="retrieved-list-header">
        检索记录
      </div>
      <div class="retrieved-list-container">
        <el-table
          :data="retrieveRecords"
          max-height="70vh"
          v-loading="retrieveRecordsTableLoading"
          element-loading-text="查询记录中"
          align="center"
        >
          <el-table-column prop="id" label="ID" width="70"/>
          <el-table-column prop="tag" label="标识 Tag" width="170">
            <template #default="scope">
              <el-link
                type="primary"
                :underline="false"
                @click="handleCheckRecordContent(scope.row.id)"
              >
                {{ scope.row.tag }}
              </el-link>
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="检索时间" width="200"/>
          <el-table-column label="代码领域" width="130">
            <template #default="scope">
              <div style="display: flex; align-items: center; column-gap: 5px">
                <img
                  :src="repoClassificationIconMap.get(scope.row.repoClassification)"
                  alt="icon"
                  style="width: 16px"
                >
                <span>
                {{ repoClassificationMap.get(scope.row.repoClassification) }}
              </span>
              </div>
            </template>
          </el-table-column>
          <el-table-column label="检索状态" width="120">
            <template #default="scope">
              {{ retrieveStatesMap.get(scope.row.retrieveState) }}
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" width="120">
            <template #default="scope">
              <el-button
                v-if="scope.row.retrieveState === retrieveStates.SUCCESS"
                type="primary"
                size="small"
                icon="view"
                @click="handleCheckResult(scope.row.id)"
              >
                查看结果
              </el-button>
              <el-button
                v-else-if="scope.row.retrieveState !== retrieveStates.RETRIEVING"
                type="info"
                size="small"
                icon="refreshRight"
                @click="handleReretrieve"
              >
                重新检索
              </el-button>
              <LoadingRect v-else/>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <el-pagination
        v-model:current-page="curPage"
        v-model:page-size="pageSize"
        :total="total"
        background
        layout="sizes, prev, pager, next, total"
        :page-sizes="[10, 15, 30, 50]"
        @current-change="getRetrieveRecords"
        @size-change="getRetrieveRecords"
      />
    </el-col>
  </el-row>
  <el-dialog
    v-model="codeFileContentDialogVisible"
    :title="codeFileList[0]?.name"
    top="5vh"
    width="75%"
  >
    <CodeBlock :code="codeFileContent" :editable="false" font-size="14px"/>
  </el-dialog>
  <el-dialog
    v-model="retrieveRecordDialogVisible"
    :title="chosenRecord?.tag"
    top="5vh"
    width="75%"
  >
    <CodeBlock :code="chosenRecord.recordContent" :editable="false" font-size="14px"/>
  </el-dialog>
</template>

<style scoped lang="scss">
.retrieve-container {
  padding: 0 30px;

  .code-area, .btn-area, .list-area {
    display: flex;
    flex-direction: column;
    row-gap: 20px;
  }

  .btn-area {
    height: 80vh;
    justify-content: center;
  }

  .upload-code-way {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .repo-classification-choose-area {
      display: flex;
      align-items: center;

      .label {
        color: #658ea7;
      }
    }
  }

  .code-input-area {
    display: flex;
    flex-direction: column;
    row-gap: 10px;

    .input-label {
      color: #658ea7;
      font-weight: bold;
    }
  }

  .code-upload-area {
    border: 2px solid #d4eaeb;
    height: 76vh;
    padding: 10px 20px;

    .uploaded-file-list {
      margin-top: 20px;
    }

    .uploaded-file-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      border: 1px solid #4594a8;
      color: #4594a8;
      width: 100%;
      height: 40px;
      border-radius: 6px;
      padding: 0 10px;
    }
  }

  .retrieved-list-header {
    height: 33.5px;
    line-height: 33.5px;
    font-size: 1.1rem;
    font-weight: bold;
    color: #4594a8;
  }

  .retrieved-list-container {
    border: 2px solid #d4eaeb;
    padding: 5px 0;
  }
}
</style>
