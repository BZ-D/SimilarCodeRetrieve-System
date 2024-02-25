import { createRouter, createWebHistory } from 'vue-router'
import {getRecord} from "@/utils/record";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: {
        path: '/index'
      }
    },
    {
      path: '/index',
      name: '主页',
      component: () => import('@/views/index/IndexView.vue'),
      redirect: {
        path: '/index/nav'
      },
      children: [
        {
          path: 'nav',
          name: '主页导航页',
          component: () => import('@/views/index/components/NavBox.vue'),
          meta: {
            title: '相似代码检索系统'
          },
        }
      ]
    },
    {
      path: '/home',
      name: '功能主页',
      component: () => import('@/views/home/HomeView.vue'),
      redirect: {
        path: '/home/statistics'
      },
      children: [
        {
          path: 'statistics',
          name: '统计数据',
          component: () => import('@/views/home/statistics/StatisticsView.vue'),
          redirect: {
            path: '/home/statistics/overall'
          },
          children: [
            {
              path: 'overall',
              name: '整体数据',
              component: () => import('@/views/home/statistics/components/OverallStatistics.vue'),
              meta: {
                title: '整体数据'
              }
            },
            {
              path: 'concrete',
              name: '具体仓库',
              component: () => import('@/views/home/statistics/components/ConcreteStatistics.vue'),
              meta: {
                title: '具体仓库'
              }
            }
          ]
        },
        {
          path: 'retrieve',
          name: '代码检索',
          redirect: {
            path: '/home/retrieve/overall'
          },
          component: () => import('@/views/home/retrieve/RetrieveView.vue'),
          meta: {
            title: '代码检索'
          },
          children: [
            {
              path: 'overall',
              name: '检索列表',
              component: () => import('@/views/home/retrieve/components/Retrieve.vue'),
              meta: {
                title: '检索列表'
              }
            },
            {
              path: 'result',
              name: '检索结果',
              component: () => import('@/views/home/retrieve/components/Result.vue'),
              meta: {
                title: '检索结果'
              }
            }
          ]
        }
      ]
    },
    {
      path: "/:catchAll(.*)", // 不识别的 path 自动匹配 404
      name: '404 Not Found',
      component: () => import('../views/404/index.vue')
    }
  ]
});

router.beforeEach(async (to, from, next) => {
  // @ts-ignore
  document.title = to.meta.title || 'SCR System';
  if (to.path.includes('/home/retrieve/result')) {
    if (!getRecord(to.query.recordId || -1)) {
      next({
        path: '/home/retrieve'
      });
    }
  }
  next();
});

export default router
