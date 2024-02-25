<script setup lang="ts">
const props = defineProps({
  text: {
    type: String,
    required: true,
    default: ''
  }
})
</script>

<template>
  <div class="btn">
    {{ props.text }}
  </div>
</template>

<style lang="scss" scoped>
.btn {
  text-decoration: none;

  border-radius: 20px;
  width: 200px;
  height: 40px;
  color: #ffffff;
  font-weight: bold;
  background: linear-gradient(90deg, #93a9d8, #7597cc, #568bd2, #4987d2);

  font-size: 18px;
  line-height: 40px;
  text-align: center;

  transition: box-shadow 0.2s;

  &:hover {
    cursor: pointer;
    box-shadow: rgba(14, 30, 37, 0.12) 0px 2px 4px 0px, rgba(14, 30, 37, 0.32) 0px 2px 16px 0px;
  }

  &:active {
    background: linear-gradient(90deg, #8499c0, #738ebb, #6186b9, #6488be);
  }
}
</style>
