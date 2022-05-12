<template>
  <v-card class="px-2 ma-1">
    
    
    <v-layout row wrap>
      <v-col class="py-2">
        <div class="px-2">
            <span class="mr-2">{{ songName }}</span>
            <span class="grey--text" style="font-size: 12px">{{ artist }}</span>
        </div>
        
        <v-chip small outlined class="ma-1" color="primary">{{ notationCategoryDisplay }}</v-chip>
        <v-chip small outlined class="ma-1" color="green">{{ playCategoryDisplay }}</v-chip>
      </v-col>
      <v-col class="d-flex align-center justify-end">
        <v-btn text>下载</v-btn>
      </v-col>

    </v-layout>

  </v-card>
</template>

<script>
  export default {
    props: {
      notationName: String,
      songName: String,
      artist: String,
      playCategoryCode: String,
      notationCategoryCode: String,
    },
    computed: {
      playCategoryDisplay: function () {
        if (this.playCategoryCode === 'TC')
          return '弹唱';

        if (this.playCategoryCode === 'ZT')
          return '指弹';

        return '';
      },
      notationCategoryDisplay: function () {
        if (this.notationCategoryCode === 'G')
          return '吉他谱';
        
        if (this.notationCategoryCode === 'U')
          return '尤克里里谱';
        
        return '';
      },
    }
            

  }
</script>