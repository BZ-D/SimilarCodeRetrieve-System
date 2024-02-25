<script setup lang="ts">
import { useRouter, useRoute } from "vue-router";
import {ref} from "vue";

const router = useRouter();
const route = useRoute();
const cm = ref<any>(null);

const handleNavClick = (toPath: string) => {
  if (route.path === toPath) return;
  router.push(toPath);
};
</script>

<template>
  <el-container class="home-layout">
    <el-header class="home-layout-header">
      <div class="header-system-name" @click="() => { router.push('/'); }">
        <img src="../../assets/pics/github.png" class="header-img" alt="">
        <div class="cn-name">
          相似代码检索系统
        </div>
      </div>
      <div class="header-nav-btns">
        <div
          class="header-nav-btn"
          :class="{ 'active': route.path.includes('/home/statistics') }"
          @click="handleNavClick('/home/statistics')"
        >
          数据统计
        </div>
        <div
          class="header-nav-btn"
          :class="{ 'active': route.path.includes('/home/retrieve') }"
          @click="handleNavClick('/home/retrieve')"
        >
          代码检索
        </div>
      </div>
      <div class="header-right-side-icons">
        <img src="../../assets/pics/android.png" class="header-img" alt="">
        <img src="../../assets/pics/microservices.png" class="header-img" alt="">
        <img src="../../assets/pics/util.png" class="header-img" alt="">
      </div>
    </el-header>
    <el-main class="home-layout-main">
      <RouterView />
    </el-main>
  </el-container>
</template>

<style scoped lang="scss">
.cur-user {
  .el-dropdown-link {
    cursor: pointer;
    color: #ffffff;
    display: flex;
    align-items: center;

    &:hover, &:focus {
      outline: none;
    }
  }
}

.home-layout-header {
  position: fixed;
  top: 0;
  width: 100%;

  background: rgba(196, 231, 231, 0.82);
  display: flex;
  align-items: center;
  justify-content: space-between;
  z-index: 210;

  .header-img {
    height: 35px;
  }

  .header-system-name {
    display: flex;
    align-items: center;
    column-gap: 20px;
    color: #4d4d4d;

    &:hover {
      cursor: pointer;
    }

    .cn-name {
      font-size: 1.1rem;
      font-weight: bold;
    }
  }

  .header-nav-btns {
    display: flex;
    column-gap: 40px;

    .header-nav-btn {
      cursor: pointer;
      color: #1596ab;
      font-size: 1.2rem;
      border: 1.5px solid #1596ab;
      border-radius: 5px;
      padding: 0 50px;

      transition: 0.2s;
    }

    .header-nav-btn.active, .header-nav-btn:hover {
      color: #fff;
      border-color: #2CA9BE;
      background: #2ca9be;
    }
  }

  .header-right-side-icons {
    display: flex;
    align-items: center;
    column-gap: 20px;
  }
}

.home-layout-main {
  overflow: auto;
  margin-top: 60px;
}

</style>
