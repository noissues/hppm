<template>
  <v-app class="grey lighten-3"> <!-- 给整个应用设置浅灰色背景 -->
    <Navbar />
    <v-main >
        <v-container class="mx-auto" style="max-width: 1080px">
          <router-view></router-view>
        </v-container>
    </v-main>
  </v-app>
</template>

<script>
import Navbar from '@/components/Navbar';

export default {
  name: 'App',

  components: {
    Navbar,
  },

  data: () => ({
    //
  }),
};
</script>
