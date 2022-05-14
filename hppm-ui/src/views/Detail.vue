<template>
  <v-row>

    <v-col sm="12" md="4" order-md="last">

      <v-sheet rounded="lg">
        <iframe frameborder="no" border="0" marginwidth="0" marginheight="0" width=100% height=86
                src="//music.163.com/outchain/player?type=2&id=28996926&auto=1&height=66">
        </iframe>

        <v-card flat v-for="(song, index) in songs" :key="song.title" class="hidden-sm-and-down">
          <v-row>
            <v-col cols="1"> <!--为其指定列宽 1-->
              <div class="d-flex justify-center">
                <span class="mt-1">{{ index + 1 }}</span>
              </div>
            </v-col>
            <v-col>
              <div class="caption">
                <a href="detail"><span class="text-subtitle-1 mr-1">{{ song.title }}</span></a>
                <span class="grey--text">{{ song.singer }}</span>
              </div>
              <v-chip x-small outlined color="green">{{ song.category }}</v-chip>
            </v-col>
          </v-row>
          <v-divider></v-divider>
        </v-card>
      </v-sheet>
    </v-col>


    <v-col sm="12" md="8">
      <v-sheet rounded="lg">
        <v-card class="">
          <v-img v-for="(item,i) in items"
                 :key="i"
                 :src="item.src"
                 contain
          ></v-img>
        </v-card>
      </v-sheet>

    </v-col>


  </v-row>

</template>


<script>
  export default {
    data() {
      return {
        items: [
          {
            src: 'http://www.jitaba.cn/upimg/allimg/2102/1-2102060604192L.png',
          },
          {
            src: 'http://www.jitaba.cn/upimg/allimg/2102/1-210206060425J5.png',
          },
          {
            src: 'https://cdn.vuetifyjs.com/images/carousel/bird.jpg',
          },
          {
            src: 'https://cdn.vuetifyjs.com/images/carousel/planet.jpg',
          },
        ],

        categories: [
          {title: 'Click Me'},
          {title: 'Click Me'},
          {title: 'Click Me'},
          {title: 'Click Me 2'},
        ],
        songs: [
          {title: '你不是真正的快乐', singer: '五月天', category: '指弹'},
          {title: '温柔', singer: '五月天', category: '指弹'},
          {title: '发如雪', singer: '周杰伦', category: '弹唱'},
        ],
      }
    },
    created() {
      this.getNotationDetail();
    },
    mounted() {
    },
    methods: {
      getNotationDetail() {
        let notationId = this.$route.params && this.$route.params.notationId;
        console.log(notationId)
        const url = `/notation/${notationId}`;
        this.$axios.get(url).then((response) => {
          console.log(response)
          if (response.data && response.data.notation) {
            const notation = response.data.notation;
            this.items = [];
            for (let i = 0; i < notation.notationUrls.length; i++) {
              this.items.push({src: notation.notationUrls[i]})
            }
            console.log(this.items)
          }
        });
      }
    },
  }
</script>