<script setup lang="ts">
const props = defineProps({
  label: {
    type: String,
    required: true
  },
  value: {
    type: [Number, String],
    required: true
  },
  icon: {
    type: String,
    required: true
  }
})
</script>

<template>
  <div class="info-rect">
    <img :src="props.icon" alt="icon" class="label-icon">
    <div class="label">
      {{ props.label }}
    </div>
    <div class="value">
      {{ props.value }}
    </div>
  </div>
</template>

<style scoped lang="scss">
.info-rect {
  height: 160px;
  width: 20%;
  border-radius: 15px;
  box-shadow: rgba(0, 0, 0, 0.1) 0px 0px 5px 0px, rgba(0, 0, 0, 0.1) 0px 0px 1px 0px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 30px 10px 20px;

  .label, .value {
    color: #4d4d4d;
    font-size: 1.5rem;
    font-weight: bold;
  }

  .label-icon {
    width: 30px;
  }

  .value {
    font-size: 2.5rem;
    color: #6791a4;
  }
}
</style>
