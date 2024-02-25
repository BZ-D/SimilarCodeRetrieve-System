<script setup lang="ts">
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";

const route = useRoute();
const router = useRouter();

const statisticsFrom = ref<string>('');

onMounted(() => {
  if (route.path.includes('/statistics/overall')) {
    statisticsFrom.value = '总体数据';
  } else {
    statisticsFrom.value = '具体仓库';
  }
});

const handleFromChange = () => {
  if (statisticsFrom.value === '总体数据') {
    router.push('/home/statistics/overall');
  } else {
    router.push('/home/statistics/concrete');
  }
}
</script>

<template>
<div class="statistics-container">
  <el-radio-group
    class="statistics-from-chooser"
    v-model="statisticsFrom"
    @change="handleFromChange"
    size="large"
  >
    <el-radio-button label="总体数据" />
    <el-radio-button label="具体仓库" />>
  </el-radio-group>
  <RouterView />
</div>
</template>

<style scoped lang="scss">
.statistics-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  row-gap: 20px;
}
</style>
