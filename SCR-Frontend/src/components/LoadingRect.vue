<script setup lang="ts">
const greenThemeColor = '#55a7bb';
</script>

<template>
<div class="loading-rect">
  <div
    v-for="i in [1, 2, 3, 4, 5, 6, 7, 8]"
    class="loading-rect-item"
    :style="{
      'background-color': greenThemeColor
    }"
  />
</div>
</template>

<style scoped lang="scss">
$rectWidth: 86px;
.loading-rect {
  width: $rectWidth;
  height: 16px;
  display: flex;
  flex-wrap: nowrap;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
}

.loading-rect-item {
  height: 90%;
  width: calc($rectWidth / 8);
  animation: 2s ease-in-out infinite rect;

  @keyframes rect {
    0% {
      opacity: .3;
    }
    25% {
      opacity: 1;
    }
    50% {
      opacity: .3;
    }
    65% {
      opacity: 1;
    }
    100% {
      opacity: .3;
    }
  }

  &:nth-child(8) {
    animation-delay: .7s;
  }
  &:nth-child(7) {
    animation-delay: .6s;
  }
  &:nth-child(6) {
    animation-delay: .5s;
  }
  &:nth-child(5) {
    animation-delay: .4s;
  }
  &:nth-child(4) {
    animation-delay: .3s;
  }
  &:nth-child(3) {
    animation-delay: .2s;
  }
  &:nth-child(2) {
    animation-delay: .1s;
  }
  &:nth-child(1) {
    animation-delay: 0s;
  }
}
</style>
