<script setup lang="ts">
import {getRecord} from "@/utils/record";
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import type {IRetrieveRecord} from "@/views/home/retrieve/components/Retrieve.vue";
import {isValidUrl} from "@/utils/status";
import {getCodeContentByRepoIdAndCodeTag, getFileContentByOssUrl, getResultListByRecordId} from "@/api/retrieve";
import CodeBlock from "@/components/CodeBlock.vue";

interface IRetrieveResult {
  repoId: number, // 所属代码仓库id
  repoName: string, // 所属代码仓库名称
  repoUrl: string, // 所属代码仓库URL
  codeTag: string, // 检索到的代码文件名
  similarity: number // 相似度
  codeContent?: string // 代码内容
}

const route = useRoute();
const router = useRouter();
const chosenRecord = ref<IRetrieveRecord>(null);
const loadingResult = ref<boolean>(false);
const loadingObsOriginCode = ref<boolean>(false);

const resultList = ref<IRetrieveResult[]>([]);

const curResultNum = ref<number>(1);
const resultCodeContent = ref<string>('');

onMounted(() => {
  chosenRecord.value = getRecord(route.query.recordId || 1)
  if (isValidUrl(chosenRecord.value.recordContent)) {
    loadingObsOriginCode.value = true;
    getFileContentByOssUrl(chosenRecord.value.recordContent).then((code) => {
      chosenRecord.value.recordContent = code;
    }).finally(() => {
      loadingObsOriginCode.value = false;
    });
  }
  getResultList(+route.query.recordId || 1);
})

const getResultList = (recordId: number) => {
  loadingResult.value = true;
  getResultListByRecordId(recordId)
    .then((res) => {
      resultList.value = [];
      res.records.forEach((result) => resultList.value.push(result));
      getCodeContent();
    })
}

const getCodeContent = () => {
  if (resultList.value.length === 0) {
    resultCodeContent.value = '暂无检索结果';
    loadingResult.value = false;
    return;
  }
  loadingResult.value = true;
  const curResult = resultList.value[curResultNum.value - 1];
  if (curResult?.codeContent) {
    resultCodeContent.value = curResult.codeContent;
  } else {
    getCodeContentByRepoIdAndCodeTag({
      repoId: curResult?.repoId,
      codeTag: curResult?.codeTag
    }).then((res: string) => {
      resultCodeContent.value = res;
    }).finally(() => {
      loadingResult.value = false;
    });
  }
}

// 点击返回按钮
const goBack = () => {
  router.push('/home/retrieve')
}
</script>

<template>
  <div class="retrieve-result-container">
    <el-row :gutter="20">
      <el-col class="origin-code-area" :span="12">
        <div class="col-header">
          <el-button
            type="primary"
            icon="back"
            style="margin-right: 20px"
            @click="goBack"
          >
            返回
          </el-button>
          {{ chosenRecord?.tag || '源代码' }}
        </div>
        <CodeBlock
          :code="chosenRecord?.recordContent || ''"
          :editable="false"
          height="80vh"
          font-size="14px"
          v-loading="loadingObsOriginCode"
          element-loading-text="加载 OBS 代码内容中"
        />
      </el-col>
      <el-col class="result-area" :span="12">
        <div class="col-header">
          检索结果
        </div>
        <CodeBlock
          :code="resultCodeContent"
          :editable="false"
          height="52vh"
          v-loading="loadingResult"
          element-loading-text="请求结果中"
          font-size="14px"
        />
        <div v-if="resultList.length > 0" class="result-detail">
          <div class="result-detail-item">
            <div class="item-label">
              GitHub 仓库名：
            </div>
            <div class="item-value">
              {{ resultList[curResultNum - 1].repoName }}
            </div>
          </div>
          <div class="result-detail-item">
            <div class="item-label">
              仓库地址：
            </div>
            <div class="item-value">
              <el-link
                type="primary"
                style="font-size: 16px"
                :href="resultList[curResultNum - 1].repoUrl"
                target="_blank"
              >
                {{ resultList[curResultNum - 1].repoUrl }}
                <el-icon>
                  <TopRight/>
                </el-icon>
              </el-link>
            </div>
          </div>
          <div class="result-detail-item">
            <div class="item-label">
              代码文件名：
            </div>
            <div class="item-value">
              {{ resultList[curResultNum - 1].codeTag }}
            </div>
          </div>
          <div class="result-detail-item">
            <div class="item-label">
              代码相似度：
            </div>
            <div class="item-value">
              {{ resultList[curResultNum - 1].similarity.toFixed(3) }}
            </div>
          </div>
        </div>
        <el-pagination
          v-model:current-page="curResultNum"
          :page-size="1"
          :total="resultList.length"
          background
          layout="total, prev, pager, next, jumper"
          @current-change="getCodeContent"
          style="margin-top: 20px"
        />
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.retrieve-result-container {
  padding: 0 20px;

  .col-header {
    font-size: 1.1rem;
    font-weight: bold;
    line-height: 42px;
    color: #5a92a6;
    margin-bottom: 10px;
  }

  .result-detail {
    border: 2px solid #d8eaeb;
    border-top: none;
    padding: 5px 10px;

    display: flex;
    flex-direction: column;

    .result-detail-item {
      min-height: 30px;
      display: flex;
      align-items: center;

      .item-label {
        width: 125px;
        color: #5a92a6;
        font-weight: bold;
        text-align: right;
      }

      .item-value {
        color: #4d4d4d;
        max-width: calc(100% - 20px - 125px);
        word-break: break-word;
      }
    }
  }
}
</style>
