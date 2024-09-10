import Vue from 'vue';
import Router from 'vue-router';
import HomePage from '@/components/HomePage.vue';
import ProjectList from '@/components/projects/ProjectList.vue';
import ScheduledTaskIndex from '@/components/scheduledTasks/ScheduledTaskIndex.vue';
import ScheduledTaskEdit from '@/components/scheduledTasks/ScheduledTaskEdit.vue';
import TableMetadataIndex from '@/components/tablemetadata/TableMetadataIndex.vue';
import TableMetadataEdit from '@/components/tablemetadata/TableMetadataEdit.vue';
import NotificationRecipientIndex from '@/components/recipients/NotificationRecipientIndex.vue';
import NotificationLogIndex from '@/components/notifications/NotificationLogIndex.vue';

Vue.use(Router);

const routes = [
  {
    path: '/',
    name: 'HomePage',
    component: HomePage
  },
  {
    path: '/projects',
    name: 'ProjectList',
    component: ProjectList
  },
  {
    path: '/services',
    name: 'ScheduledTaskIndex',
    component: ScheduledTaskIndex
  },
  {
    path: '/services/:serviceName',
    name: 'ScheduledTaskEdit',
    component: ScheduledTaskEdit,
    props: true
  },
  {
    path: '/tables',
    name: 'TableMetadataIndex',
    component: TableMetadataIndex
  },
  {
    path: '/tables/:id',
    name: 'TableMetadataEdit',
    component: TableMetadataEdit,
    props: true
  },
  {
    path: '/recipients',
    name: 'NotificationRecipientIndex',
    component: NotificationRecipientIndex
  },
  {
    path: '/notificationlogs',
    name: 'NotificationLogIndex',
    component: NotificationLogIndex
  }
  
];

const router = new Router({
  mode: 'history',
  routes
});

export default router;
