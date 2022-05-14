<template>
  <v-hover v-slot="{ hover }">
    <v-card
            flat
            :color="notation.color"
            :class="{ 'on-hover': hover }"
    >
      <div class="d-flex flex-no-wrap align-center">
        <v-avatar
                rounded
                size="56"
                class="ma-3"
        >
          <v-img :src="notation.src"></v-img>
        </v-avatar>
        <v-row>
          <v-col cols="6">
            <div class="text-no-wrap text-truncate">{{ notationProp.notationName }}</div>
            <v-chip small outlined class="ma-0" color="primary">{{ playCategoryDisplay }}</v-chip>
            <v-chip small outlined class="ma-1" color="green">{{ notationCategoryDisplay }}</v-chip>
          </v-col>
          <v-col>
            <div class="text-no-wrap text-truncate" >{{ notationProp.songName }}</div>
            <span class="subtitle-2 grey--text text-no-wrap text-truncate" >{{ notationProp.artist }}</span>
          </v-col>
        </v-row>
      </div>
    </v-card>
  </v-hover>
</template>

<script>
  export default {
    name: "NotationItem",

    data: () => ({
      hover: false,
      notation:
          {
            // color: 'rgba(255, 255, 255, 0.85)',
            // src: 'https://cdn.vuetifyjs.com/images/cards/halcyon.png',
            src: require("../assets/images/guitar3.jpg"),
            title: '半句再见',
            artist: '孙燕姿',
          },
    }),

    props: {
      notationProp: Object,
      notationName: String,
      songName: String,
      artist: String,
      playCategoryCode: String,
      notationCategoryCode: String,
    },
    computed: {
      notationItemClass() {
        if (this.hover) 
          return "notationItem focus";
        return "notationItem";
      },
      playCategoryDisplay: function () {
        if (this.notationProp.playCategoryCode === 'TC')
          return '弹唱';

        if (this.notationProp.playCategoryCode === 'ZT')
          return '指弹';

        return '';
      },
      notationCategoryDisplay: function () {
        if (this.notationProp.notationCategoryCode === 'G')
          return '吉他谱';

        if (this.notationProp.notationCategoryCode === 'U')
          return '尤克里里谱';

        return '';
      },
    }
  }


</script>

<style scoped>

  .v-card.on-hover {
    transition: all 0.3s;
    background-color: #f5f5f7;
  }
</style>