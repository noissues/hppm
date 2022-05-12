<template>
  <!--  <nav>-->
  <!--    &lt;!&ndash;提示语&ndash;&gt;-->
  <!--    <v-snackbar v-model="showSnackbar" :timeout="4000" top color="success">-->
  <!--      <span>🎉😘 提交成功！( •̀ ω •́ )y </span>-->
  <!--      <template v-slot:action="{ attrs }">-->
  <!--        <v-btn-->
  <!--                color="white"-->
  <!--                text-->
  <!--                v-bind="attrs"-->
  <!--                @click="snackbar = false"-->
  <!--        >-->
  <!--          关闭-->
  <!--        </v-btn>-->
  <!--      </template>-->
  <!--    </v-snackbar>-->

  <!--    <v-app-bar app  elevation="3" color="rgba(255, 255, 255, 0.85)" style="backdrop-filter: saturate(180%) blur(20px)">-->
  <!--      <v-container class="mx-auto py-0">-->
  <!--        <v-row align="center" >-->
  <!--          -->
  <!--          -->
  <!--          <v-toolbar-title class="grey&#45;&#45;text">-->
  <!--            <router-link to="/" style="text-decoration: none;">-->
  <!--              <span class="font-weight-light">HP</span>-->
  <!--              <span>PM</span>-->
  <!--            </router-link>-->
  <!--          </v-toolbar-title>-->

  <!--          <v-spacer></v-spacer>-->
  <!--          &lt;!&ndash;        <v-autocomplete&ndash;&gt;-->
  <!--          &lt;!&ndash;                cache-items&ndash;&gt;-->
  <!--          &lt;!&ndash;                class="mx-4"&ndash;&gt;-->
  <!--          &lt;!&ndash;                style="max-width: 450px"&ndash;&gt;-->
  <!--          &lt;!&ndash;                dense&ndash;&gt;-->
  <!--          &lt;!&ndash;                hide-details&ndash;&gt;-->
  <!--          &lt;!&ndash;                hide-no-data&ndash;&gt;-->
  <!--          &lt;!&ndash;                label="搜索歌名或歌手"&ndash;&gt;-->
  <!--          &lt;!&ndash;                solo&ndash;&gt;-->
  <!--          &lt;!&ndash;                append-outer-icon=""&ndash;&gt;-->
  <!--          &lt;!&ndash;                append-icon="mdi-card-search-outline"&ndash;&gt;-->
  <!--          &lt;!&ndash;                @click:append="searchByParam"&ndash;&gt;-->
  <!--          &lt;!&ndash;                @keyup="searchParam"&ndash;&gt;-->
  <!--          &lt;!&ndash;                v-model="searchParam"&ndash;&gt;-->
  <!--          &lt;!&ndash;        ></v-autocomplete>&ndash;&gt;-->
  <!--          <v-text-field-->
  <!--                  solo-inverted-->
  <!--                  flat-->
  <!--                  label="搜索歌名或歌手"-->
  <!--                  style="max-width: 450px;"-->
  <!--                  append-icon="mdi-magnify"-->
  <!--                  @click:append="searchByParam"-->
  <!--                  @keyup.enter="searchByParam"-->
  <!--                  v-model="searchParam"-->
  <!--          >-->
  <!--          </v-text-field>-->

  <!--          <v-spacer></v-spacer>-->
  <!--          -->
  <!--          <UploadPop @notationSubmitted="showSnackbar = true"/>-->
  <!--        </v-row>-->
  <!--      </v-container>-->


  <!--    </v-app-bar>-->

  <v-app-bar app elevation="3" color="rgba(255, 255, 255, 0.85)" style="backdrop-filter: saturate(180%) blur(20px)">
    <v-app-bar-nav-icon
            class="hidden-md-and-up"
            @click="toggleDrawer"
    />

    <v-container class="fill-height py-0" style="width: 1080px">

      <v-toolbar-title class="text-h4 font-weight-bold mr-5">
        <router-link to="/" style="text-decoration: none;">
          <span>HP</span>
          <span class="grey--text">PM</span>
        </router-link>
      </v-toolbar-title>


      
      <v-btn
              class="text-subtitle-1 font-weight-bold hidden-sm-and-down"
              text

              @click="toHome"
      >
        首页
      </v-btn>

      <v-btn
              class="text-subtitle-1 font-weight-bold hidden-sm-and-down"
              text
              @click="toAdmin"
      >
        曲谱管理
      </v-btn>

      <v-spacer></v-spacer>
      
      <v-text-field
              rounded
              append-icon="mdi-magnify"
              flat
              hide-details
              solo-inverted

              label="搜索歌名或歌手"
              style="max-width: 400px;"
              @click:append="searchByParam"
              @keyup.enter="searchByParam"
              v-model="searchParam"
      />
    </v-container>
  </v-app-bar>
  <!--  </nav>-->
</template>

<script>
  // import UploadPop from "./UploadPop";
  import Bus from '../bus.js';

  export default {
    name: "Navbar",

    components: {
      // UploadPop
    },

    data() {
      return {
        searchParam: "",
        showSnackbar: false,
      }
    },

    methods: {
      searchByParam() {
        Bus.$emit('searchOnClick', this.searchParam);
      },
      toHome() {
        this.$router.push({ name: 'Home' });
      },
      toAdmin() {
        this.$router.push({ name: 'Admin' });
      }
    }
  }
</script>

<style scoped>

</style>