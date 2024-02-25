<script setup lang="ts">
const curDate = new Date();
const numMap = new Map([
  [0, "日"],
  [1, "一"],
  [2, "二"],
  [3, "三"],
  [4, "四"],
  [5, "五"],
  [6, "六"]
]);
</script>

<template>
  <el-container class="index-container">
    <el-header class="index-header">
      <div class="index-system-name">
        <div class="cn-name">
          相似代码检索系统
        </div>
        <div class="en-name">
          Similar Code Retrieval System
        </div>
      </div>
    </el-header>
    <el-main class="index-main">
      <div class="index-main-box">
        <img src="@/assets/pics/nav-header.png" alt="" class="index-main-header-img">
        <div class="welcome-text">
          <div class="welcome-cn-text">
            欢迎使用系统
          </div>
          <div class="welcome-en-text">
            WELCOME TO THE SYSTEM
          </div>
        </div>

        <RouterView />

        <div class="cur-date">
          今天日期：{{ `${curDate.getFullYear()}年${curDate.getMonth() + 1}月${curDate.getDate()}日  星期${numMap.get(curDate.getDay())}`
          }}
        </div>
      </div>
    </el-main>
  </el-container>
</template>

<style lang="scss" scoped>
.index-container {
  height: 100vh;
  background: url("@/assets/pics/bg.png") no-repeat;
  background-size: cover;
  background-attachment: fixed;
  background-position: center;
}

.index-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: auto;
  padding: 30px 60px 0;

  .index-system-name {
    color: #2a2a2a;
    text-align: center;

    .cn-name {
      font-size: 3rem;
      font-weight: bold;
    }

    .en-name {
      font-size: 1.2rem;
    }
  }
}

.index-main {
  position: relative;

  .index-main-box {
    box-shadow: rgba(14, 30, 37, 0.12) 0px 2px 4px 0px, rgba(14, 30, 37, 0.32) 0px 2px 16px 0px;
    position: absolute;
    right: 100px;
    top: 50px;
    background: #ffffff;

    width: 550px;
    height: 350px;
    border-radius: 13px;

    .index-main-header-img {
      width: 551px;
      margin-top: -1px;
    }

    .welcome-text {
      margin: 20px 0;
      display: flex;
      align-items: center;
      row-gap: 5px;
      flex-direction: column;

      .welcome-cn-text {
        font-size: 30px;
        font-weight: bold;
        color: #a00000;
        background-image: -webkit-linear-gradient(top, #38eec3, #46dedd, #35cde5, #56a9d2);
        background-clip: text;
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
      }

      .welcome-en-text {
        color: #bfbfbf;
        font-size: 13px;
      }
    }

    .cur-date {
      text-align: center;
      color: #878787;
      font-size: 13px;
      position: absolute;
      bottom: 15px;
      width: 100%;
    }
  }
}
</style>
