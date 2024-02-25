<script setup lang="ts">
import {Codemirror} from "vue-codemirror";
import {java} from "@codemirror/lang-java";
import {ref, watch} from "vue";

const props = defineProps({
  code: {
    type: String,
    default: ''
  },
  height: {
    type: String,
    default: '70vh'
  },
  fontSize: {
    type: String,
    default: '16px'
  },
  editable: {
    type: Boolean,
    default: true
  }
});

const codeVal = ref<string>(props.code);
const cmEditor = ref<any>(null);

const lang = java();

watch(
  () => props.code,
  (newCode, oldCode) => {
    codeVal.value = newCode;
  }
);

defineExpose({
  codeVal
});
</script>

<template>
  <div
    class="cm-container"
    :style="{
      'height': props.height
    }"
  >
    <codemirror
      ref="cmEditor"
      :extensions="[lang]"
      :tab-size="4"
      v-model="codeVal"
      :disabled="!editable"
      :style="{
        'font-size': props.fontSize
      }"
    />
  </div>
</template>

<style scoped lang="scss">
.cm-container {
  width: 100%;
  border: 2px solid #d4eaeb;
  overflow: auto;
}
</style>
