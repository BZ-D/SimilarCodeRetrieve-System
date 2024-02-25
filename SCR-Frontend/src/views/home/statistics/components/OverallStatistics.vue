<script setup lang="ts">
import RepoChart from "@/views/home/statistics/components/RepoChart.vue";
import {onMounted, reactive, ref} from "vue";
import {repoClassifications} from "@/common/retrieve";
import {getCodeRepoList} from "@/api/repo";
import {repoClassificationIconMap} from "@/common/retrieve";
import BasicInfoRect from "@/views/home/statistics/components/BasicInfoRect.vue";

const loading = ref<boolean>(false);

const repoCounts = reactive({
  [repoClassifications.FULL]: 0,
  [repoClassifications.ANDROID]: 0,
  [repoClassifications.MICROSERVICE]: 0,
  [repoClassifications.UTIL]: 0
});

onMounted(() => {
  getOverallCounts();
})

const getOverallCounts = () => {
  loading.value = true;
  const androidP = getCodeRepoList({repoClassification: repoClassifications.ANDROID, pageIndex: 1, pageSize: 1})
    .then(res => {
      repoCounts[repoClassifications.FULL] += res.total;
      repoCounts[repoClassifications.ANDROID] = res.total;
    });
  const microserviceP = getCodeRepoList({
    repoClassification: repoClassifications.MICROSERVICE,
    pageIndex: 1,
    pageSize: 1
  })
    .then(res => {
      repoCounts[repoClassifications.FULL] += res.total;
      repoCounts[repoClassifications.MICROSERVICE] = res.total;
    });
  const utilP = getCodeRepoList({repoClassification: repoClassifications.UTIL, pageIndex: 1, pageSize: 1})
    .then(res => {
      repoCounts[repoClassifications.FULL] += res.total;
      repoCounts[repoClassifications.UTIL] = res.total;
    });
  Promise.all([androidP, microserviceP, utilP]).then(() => {
    loading.value = false;
  });
}
</script>

<template>
  <div class="overall-statistics-container">
    <div
      class="basic-statistics-container"
      v-loading="loading"
      element-loading-text="统计数据中"
    >
      <BasicInfoRect
        :icon="repoClassificationIconMap.get(repoClassifications.FULL)"
        :value="repoCounts[repoClassifications.FULL]"
        label="仓库总数"
      />
      <BasicInfoRect
        :icon="repoClassificationIconMap.get(repoClassifications.ANDROID)"
        :value="repoCounts[repoClassifications.ANDROID]"
        label="安卓应用"
      />
      <BasicInfoRect
        :icon="repoClassificationIconMap.get(repoClassifications.MICROSERVICE)"
        :value="repoCounts[repoClassifications.MICROSERVICE]"
        label="微服务应用"
      />
      <BasicInfoRect
        :icon="repoClassificationIconMap.get(repoClassifications.UTIL)"
        :value="repoCounts[repoClassifications.UTIL]"
        label="工程工具"
      />
    </div>
    <RepoChart/>
  </div>
</template>

<style scoped lang="scss">
.overall-statistics-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  row-gap: 20px;

  .basic-statistics-container {
    border: 2px solid #dceaeb;
    height: 200px;
    width: 70vw;
    display: flex;
    justify-content: center;
    align-items: center;
    column-gap: 30px;
  }
}
</style>
