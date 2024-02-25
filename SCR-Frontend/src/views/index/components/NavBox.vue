<script setup lang="ts">
import GradientBtn from "@/views/index/components/GradientBtn.vue";
</script>

<template>
  <div>
    <div class="nav-row">
      <RouterLink to="/home/statistics">
        <GradientBtn text="数据统计" />
      </RouterLink>
      <RouterLink to="/home/retrieve">
        <GradientBtn text="代码检索" />
      </RouterLink>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.nav-row {
  display: flex;
  justify-content: center;
  column-gap: 20px;
}

.nav-row + .nav-row {
  margin-top: 25px;
}
</style>
