<script setup lang="ts">
import * as echarts from 'echarts';
import {onMounted, ref, reactive, watch} from "vue";
import {getCodeRepoStatistics} from "@/api/repo";

enum metrics {
  COMMIT = 'Commit数',
  FILE = '受影响文件数',
  ADD = '新增代码行',
  MINUS = '减少代码行'
}

const charts = reactive({
  chart1: null,
  chart2: null
});
const loading = ref<boolean>(false);

const props = defineProps({
  width: {
    type: String,
    default: '70vw'
  },
  height: {
    type: String,
    default: '500px'
  },
  repoId: {
    type: Number,
    default: 0
  }
});

const colors1 = ['#55a7bb', '#7189d2'];
const colors2 = ['#1f9137', '#cb2f2f'];

let option1 = {
  color: colors1,
  title: {
    text: 'Commits & Affected Files',
    textStyle: {
      color: '#5a92a6',
      fontSize: '14px'
    }
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross'
    }
  },
  legend: {
    data: [metrics.COMMIT, metrics.FILE]
  },
  xAxis: [
    {
      type: 'category',
      axisTick: {
        alignWithLabel: true
      },
      data: []
    }
  ],
  yAxis: [
    {
      type: 'value',
      name: metrics.COMMIT,
      position: 'left',
      alignTicks: true,
      axisLine: {
        show: true,
        lineStyle: {
          color: colors1[0]
        }
      },
      axisLabel: {
        formatter: '{value}'
      }
    },
    {
      type: 'value',
      name: metrics.FILE,
      position: 'right',
      alignTicks: true,
      axisLine: {
        show: true,
        lineStyle: {
          color: colors1[1]
        }
      },
      axisLabel: {
        formatter: '{value}'
      }
    }
  ],
  series: [
    {
      name: metrics.COMMIT,
      type: 'bar',
      data: []
    },
    {
      name: metrics.FILE,
      type: 'bar',
      yAxisIndex: 1,
      data: []
    }
  ]
};

let option2 = {
  color: colors2,
  title: {
    text: 'Additions & Deletions',
    textStyle: {
      color: '#5a92a6',
      fontSize: '14px'
    }
  },
  tooltip: {
    trigger: 'axis',
    axisPointer: {
      type: 'cross'
    }
  },
  legend: {
    data: [metrics.ADD, metrics.MINUS]
  },
  xAxis: [
    {
      type: 'category',
      axisTick: {
        alignWithLabel: true
      },
      data: []
    }
  ],
  yAxis: [
    {
      type: 'value',
      position: 'left',
      alignTicks: true,
      axisLine: {
        show: true,
        lineStyle: {
          color: colors1[0]
        }
      },
      axisLabel: {
        formatter: function(value) {
          if (value === 0) return '0';
          if (value > 0) return `+${value}`;
          return `${value}`;
        }
      }
    }
  ],
  series: [
    {
      name: metrics.ADD,
      type: 'line',
      data: []
    },
    {
      name: metrics.MINUS,
      type: 'line',
      data: []
    }
  ]
};

onMounted(() => {
  drawCharts();
});

watch(
  () => props.repoId,
  () => {
    drawCharts();
  }
)

const clearChartData = () => {
  option1.xAxis[0].data = []; // x 轴日期
  option2.xAxis[0].data = []; // x 轴日期
  option1.series[0].data = []; // commit数
  option1.series[1].data = []; // 受影响文件数
  option2.series[0].data = []; // 增加代码行
  option2.series[1].data = []; // 增加代码行
}

const drawCharts = () => {
  if (charts.chart1) {
    charts.chart1.dispose();
    charts.chart2.dispose();
  }
  const chart1 = echarts.init(document.querySelector('#echarts-container-1'));
  const chart2 = echarts.init(document.querySelector('#echarts-container-2'));
  loading.value = true;
  getCodeRepoStatistics(props.repoId)
    .then((res) => {
      clearChartData();
      for (const info of res) {
        option1.xAxis[0].data.push(info.date); // x 轴日期
        option2.xAxis[0].data.push(info.date); // x 轴日期
        option1.series[0].data.push(info.commitsCount); // commit数
        option1.series[1].data.push(info.affectFilesCount); // 受影响文件数
        option2.series[0].data.push(info.addLinesCount); // 增加代码行
        option2.series[1].data.push(-info.minusLinesCount); // 增加代码行
      }
      chart1.setOption(option1);
      chart2.setOption(option2);
      charts.chart1 = chart1;
      charts.chart2 = chart2;
    })
    .finally(() => {
      loading.value = false;
    })
}

</script>

<template>
  <div
    id="echarts-container-1"
    class="echarts-container"
    :style="{
      'width': props.width,
      'height': props.height
    }"
    v-loading="loading"
    element-loading-text="绘制统计图表中"
  />

  <div
    id="echarts-container-2"
    class="echarts-container"
    :style="{
      'width': props.width,
      'height': props.height
    }"
    v-loading="loading"
    element-loading-text="绘制统计图表中"
  />
</template>

<style scoped lang="scss">
.echarts-container {
  border: 2px solid #d8eaeb;
  padding: 20px 10px 10px;

  & + & {
    margin-top: 20px;
  }
}
</style>
