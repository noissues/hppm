<template>


  <v-row>
    <v-col>
      <v-tabs v-model="tab">
        <v-tab :key="G" @click="tabChange('G')">吉他谱</v-tab>
        <v-tab :key="U" @click="tabChange('U')">尤克里里谱</v-tab>

        <v-spacer></v-spacer>

        <!-- 筛选下拉框 -->
        <v-menu offset-y>
          <template v-slot:activator="{ on, attrs }">
            <v-btn text class="align-self-center" v-bind="attrs" v-on="on">
              筛选
              <v-icon right>mdi-menu-down</v-icon>
            </v-btn>
          </template>

          <v-list class="grey lighten-3">
            <v-list-item>
              指弹
            </v-list-item>
            <v-list-item>
              弹唱
            </v-list-item>
          </v-list>
        </v-menu>
      </v-tabs>

      <v-divider></v-divider>


      <v-tabs-items v-model="tab">
        <!--吉他谱-->
        <v-tab-item :key="G">
          <NotationItem
                  class="my-2"
                  v-for="(notation, index) in notations"
                  :key="index"
                  :notationProp="notation"
                  @click.native="showDetail(notation)"
          />
          <v-pagination v-model="page"
                        :length="totalPages"
                        total-visible="7"
                        v-show="totalPages > 1"
                        @input="handlePageChange"
          ></v-pagination>
        </v-tab-item>

        <!--尤克里里谱-->
        <v-tab-item :key="U">
          <v-card flat v-for="song in notations" :key="song.id" @click="showDetail(song)">
            <SongCard hidden class="mt-2" style="display: block"
                      v-bind:notationName="song.notationName"
                      v-bind:songName="song.songName"
                      v-bind:artist="song.artist"
                      v-bind:playCategoryCode="song.playCategoryCode"
                      v-bind:notationCategoryCode="song.notationCategoryCode"
            />
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    </v-col>
  </v-row>
</template>

<script>
  // @ is an alias to /src
  // import HelloWorld from '@/components/HelloWorld.vue'
  import SongCard from "@/components/SongCard";
  import Bus from "../bus.js";
  import NotationItem from "../components/NotationItem";

  export default {
    name: 'Home',

    components: {
      NotationItem,
      SongCard,
    },

    data() {
      return {
        tab: null,
        notationCategory: 'G',
        categories: [
          {title: 'Click Me'},
          {title: 'Click Me'},
          {title: 'Click Me'},
          {title: 'Click Me 2'},
        ],
        notations: [
          {notationName: '白桦林入门简单版', songName: '白桦林', artist: '朴树', notationCategoryCode: 'G', playCategoryCode: 'TC'},
          {notationName: '温柔入门简单版', songName: '温柔', artist: '五月天', notationCategoryCode: 'G', playCategoryCode: 'TC'},
          {notationName: '发如雪入门简单版', songName: '发如雪', artist: '周杰伦', notationCategoryCode: 'G', playCategoryCode: 'TC'},
        ],

        // 分页相关
        page: 1,
        totalPages: 0,
        pageSize: 5,
      }
    },
    mounted() {
      this.getAllNotations();
    },
    methods: {
      tabChange(category) {
        this.notationCategory = category;
        this.getAllNotations();
      },
      handlePageChange(page) {
        this.page = page;
        this.getAllNotations();
      },
      getAllNotations() {
        let param = {"notationCategory": this.notationCategory, "page": this.page, "size": this.pageSize};
        this.queryNotations(param);
      },
      getNotationsBySearchParam(searchParam) {
        let param = {
          "notationCategory": this.notationCategory,
          "searchParam": searchParam,
          "page": this.page,
          "size": this.pageSize
        };
        this.queryNotations(param);
      },
      queryNotations(param) {
        this.$axios.get("/notations", {params: param}).then((response) => {
          const {rows, totalPages} = response.data;

          if (rows) {
            // this.notations = notations.map(this.getDisplayNotations);
            this.notations = rows;
            this.totalPages = totalPages;
          }
        });
      },
      showDetail(song) {
        console.log('hhh');
        console.log(song);
        this.$router.push({
          path: `/detail/${song.id}`
        })
      },

      /**
       * 格式化页面展示
       * @param notation
       * @returns {{songName: (string|methods.songName), artist: (string|methods.artist), id: *, playCategory: string}}
       */
      getDisplayNotations(notation) {
        console.log(notation)
        let playCategoryDisplay = '';
        if (notation.playCategory == 'TC') {
          console.log('TC')
          playCategoryDisplay = '弹唱';
        }
        if (notation.playCategory == 'ZT') {
          console.log('ZT')
          playCategoryDisplay = '指弹';
        }
        return {
          id: notation.id,
          songName: notation.songName, //.substr(0, 40) + "...",
          artist: notation.artist, //.substr(0, 40) + "...",
          playCategory: playCategoryDisplay,
        };
      },
    },
    created() {
      Bus.$on('searchOnClick', searchParam => {
        console.log(searchParam);
        this.getNotationsBySearchParam(searchParam);
      });
    }


  }
</script>
