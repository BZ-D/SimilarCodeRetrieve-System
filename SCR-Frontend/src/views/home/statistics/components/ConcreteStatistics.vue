<script setup lang="ts">
import {onMounted, ref, watch} from "vue";
import {repoClassificationMap, repoClassifications} from "@/common/retrieve";
import {getCodeRepoList} from "@/api/repo";
import RepoChart from "@/views/home/statistics/components/RepoChart.vue";

const chosenClassification = ref(repoClassifications.ANDROID);
const loading = ref<boolean>(false);
const curPage = ref<number>(1);
const pageSize = ref<number>(10);
const total = ref<number>(0);

const chosenRepoId = ref<number>(-1);

const repoList = ref<{
  id: number,
  repoName: string,
  repoUrl: string
}[]>([]);

const rightColWidth = ref<string>('');

onMounted(() => {
  getCodeRepoListData();
  // 获取右侧 col 的宽度，传给 echarts
  const el = document.querySelector('.certain-repo-container');
  rightColWidth.value = el.clientWidth + 'px';
})

watch(
  chosenClassification,
  () => {
    curPage.value = 1;
    getCodeRepoListData();
  }
)

const getCodeRepoListData = () => {
  loading.value = true;
  chosenRepoId.value = -1;
  getCodeRepoList({
    pageSize: pageSize.value,
    pageIndex: curPage.value,
    repoClassification: chosenClassification.value
  }).then((res) => {
    repoList.value = [];
    res.records.forEach((repo) => repoList.value.push(repo));
    total.value = res.total;
  }).finally(() => {
    loading.value = false;
  });
}

const handleCheckRepo = (repoId: number) => {
  chosenRepoId.value = repoId;
}
</script>

<template>
  <div class="concrete-statistics-container">
    <el-row :gutter="30">
      <el-col class="repo-list-info-container" :span="10">
        <div class="repo-classification-choose-area">
          <div class="label">
            仓库领域：
          </div>
          <el-select
            v-model="chosenClassification"
          >
            <el-option :label="repoClassificationMap.get(repoClassifications.ANDROID)"
                       :value="repoClassifications.ANDROID"/>
            <el-option :label="repoClassificationMap.get(repoClassifications.MICROSERVICE)"
                       :value="repoClassifications.MICROSERVICE"/>
            <el-option :label="repoClassificationMap.get(repoClassifications.UTIL)" :value="repoClassifications.UTIL"/>
          </el-select>
        </div>
        <div class="repo-table-container">
          <el-table
            :data="repoList"
            max-height="62vh"
            v-loading="loading"
            element-loading-text="加载仓库列表中"
            align="center"
          >
            <el-table-column prop="id" label="ID" width="80"/>
            <el-table-column prop="repoName" label="仓库名" width="180"/>
            <el-table-column prop="repoUrl" label="仓库地址" width="100">
              <template #default="scope">
                <el-link type="primary" icon="top-right" :href="scope.row.repoUrl" target="_blank">
                  前往
                </el-link>
              </template>
            </el-table-column>
            <el-table-column prop="repoUrl" label="统计数据" width="120">
              <template #default="scope">
                <el-button
                  type="primary"
                  :plain="scope.row.id !== chosenRepoId"
                  icon="histogram"
                  size="small"
                  @click="handleCheckRepo(scope.row.id)"
                >
                  查看
                </el-button>
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
          :page-sizes="[10, 20, 30]"
          @current-change="getCodeRepoListData"
          @size-change="getCodeRepoListData"
        />
      </el-col>
      <el-col :span="14">
        <div class="certain-repo-container">
          <div v-if="chosenRepoId < 0" class="no-choose-hint">
            请选择一个仓库以查看其具体统计数据。
          </div>
          <RepoChart v-else :width="rightColWidth" height="38vh" :repo-id="chosenRepoId"/>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<style scoped lang="scss">
.concrete-statistics-container {
  width: 100%;
  padding: 0 20px;

  .repo-list-info-container {
    border: 2px solid #dbe9eb;
    padding: 20px 0;
    max-height: 80vh;

    display: flex;
    flex-direction: column;
    align-items: center;

    .repo-classification-choose-area {
      margin-bottom: 10px;
      text-align: center;

      .label {
        display: inline;
        color: #4d4d4d;
      }
    }

    .repo-table-container {
      width: 100%;
      margin-bottom: 10px;
    }
  }

  .certain-repo-container {
    .no-choose-hint {
      color: #658ea7;
    }
  }
}
</style>
