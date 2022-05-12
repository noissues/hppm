<template>
  <v-data-table
          :headers="headers"
          :items="notations"
          :server-items-length="total"
          :options.sync="options"
          class="elevation-1"
  >
    <template v-slot:top>
      <v-toolbar
              flat
      >
        <v-toolbar-title>曲谱管理</v-toolbar-title>
        <v-divider
                class="mx-4"
                inset
                vertical
        ></v-divider>
        <v-spacer></v-spacer>


        <v-dialog
                v-model="dialog"
                max-width="500px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-btn
                    color="primary"
                    dark
                    class="mb-2"
                    v-bind="attrs"
                    v-on="on"
            >
              上传曲谱
            </v-btn>
          </template>
          <v-card>
            <v-card-title>
              <span class="text-h5">{{ formTitle }}</span>
            </v-card-title>

            <v-card-text>
              <v-container>

                <v-row>
                  <v-col cols="12">
                    <v-file-input
                            ref="uploadNotationImage"
                            v-model="files"
                            multiple
                            small-chips
                            accept="image/png, image/jpeg, image/bmp"
                            placeholder="选择曲谱图片"
                            prepend-icon="mdi-paperclip"
                            @change="onUploadFile"
                    ></v-file-input>
                    <div id="thumbnailDiv">
                      <v-avatar class="mr-2" v-for="(item, index) in thumbnailUrls" :key="index" rounded tile
                                draggable="true" size="70">
                        <v-img :src="item"/>
                      </v-avatar>
                    </div>

                    <v-text-field label="曲谱名" v-model="editedItem.notationName"></v-text-field>
                    <v-text-field label="歌曲名" v-model="editedItem.songName"></v-text-field>
                    <v-text-field label="歌手" v-model="editedItem.artist"></v-text-field>
                  </v-col>
                  <v-col cols="6">
                    <v-select
                            v-model="editedItem.notationCategorySelect"
                            :items="notationCategoryItems"
                            item-text="name"
                            item-value="abbr"
                            label="曲谱类型"
                            persistent-hint
                            return-object
                            single-line
                    ></v-select>
                  </v-col>
                  <v-col cols="6">
                    <v-select
                            v-model="editedItem.playCategorySelect"
                            :items="playCategoryItems"
                            item-text="name"
                            item-value="abbr"
                            label="演奏类型"
                            persistent-hint
                            return-object
                            single-line
                    ></v-select>
                  </v-col>
                </v-row>
              </v-container>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                      color="blue darken-1"
                      text
                      @click="close"
              >
                取消
              </v-btn>
              <v-btn
                      color="blue darken-1"
                      text
                      @click="save"
              >
                保存
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
        <v-dialog v-model="dialogDelete" max-width="500px">
          <v-card>
            <v-card-title class="text-h5">确认删除？</v-card-title>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="blue darken-1" text @click="closeDelete">取消</v-btn>
              <v-btn color="blue darken-1" text @click="deleteItemConfirm">确定</v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </v-toolbar>
    </template>

    <!--可以使用动态插槽 item.<name> 来自定义某些列-->
    <template v-slot:item.notationCategory="{ item }">
      <v-chip
              color='blue'
              dark
      >
        {{ item.notationCategoryCode | getNotionCategory }}
      </v-chip>
    </template>
    <template v-slot:item.playCategory="{ item }">
      <v-chip
              color='green'
              dark
      >
        {{ item.playCategoryCode | getPlayCategory }}
      </v-chip>
    </template>
    <template v-slot:item.actions="{ item }">
      <v-icon
              small
              class="mr-2"
              @click="editItem(item)"
      >
        mdi-pencil
      </v-icon>
      <v-icon
              small
              @click="deleteItem(item)"
      >
        mdi-delete
      </v-icon>
    </template>
    <template v-slot:no-data>
      <v-btn
              color="primary"
              @click="initialize"
      >
        Reset
      </v-btn>
    </template>
  </v-data-table>
</template>


<script>
  import Sortable from 'sortablejs'

  export default {

    name: "Admin",

    components: {},

    data: () => ({
      dialog: false,
      dialogDelete: false,
      headers: [
        {text: '曲谱名', align: 'start', value: 'notationName',},
        {text: '歌曲名', value: 'songName'},
        {text: '歌手', value: 'artist'},
        {text: '曲谱类型', value: 'notationCategory'},
        {text: '演奏类型', value: 'playCategory'},
        {text: '操作', value: 'actions', sortable: false},
      ],

      // data-table 的 options.sync 
      options: {},

      // 曲谱类型

      notationCategoryItems: [
        {name: '吉他', code: 'G'},
        {name: '尤克里里', code: 'U'},
      ],
      // 演奏类型

      playCategoryItems: [
        {name: '弹唱', code: 'TC'},
        {name: '指弹', code: 'ZT'},
      ],
      notations: [],
      total: 0,
      loading: true,

      // 区分编辑还是新增
      editedIndex: -1,

      // 编辑
      editedItem: {
        notationUrls: [],
        notionName: '',
        songName: '',
        artist: '',
        notationCategorySelect: {name: '曲谱类型', code: ''},
        playCategorySelect: {name: '演奏类型', code: ''},
      },

      // 默认数据
      defaultItem: {
        notationUrls: [],
        notionName: '',
        songName: '',
        artist: '',
        notationCategorySelect: {name: '曲谱类型', code: ''},
        playCategorySelect: {name: '演奏类型', code: ''},
      },

      // 文件上传组件
      files: [],
      thumbnailUrls: [],
    }),

    computed: {
      formTitle() {
        return this.editedIndex === -1 ? '新增曲谱' : '编辑曲谱'
      },
    },

    watch: {
      options: {
        handler() {
          this.queryAllNotations();
        },
        deep: true,
      },
      dialog(val) {
        if (val) {
          this.onPopUp();
        } else {
          this.close();
        }
      },
      dialogDelete(val) {
        val || this.closeDelete()
      },
    },

    created() {
      this.initialize()
    },

    methods: {
      initialize() {
        this.notations = [
          {
            notionName: '寂寞的季节（初级）',
            songName: '寂寞的季节',
            artist: '陶喆',

            // 这是后台返回的 code，前台需要做个转换，转成 select
            notationCategoryCode: 'G',
            notationCategorySelect: {name: '吉他', code: 'G'},
            playCategoryCode: 'TC',
            playCategorySelect: {name: '弹唱', code: 'TC'},
          },
          {
            notionName: '寂寞的季节吉他进阶',
            songName: '寂寞的季节',
            artist: '陶喆',
            notationCategoryCode: 'G',
            notationCategorySelect: {name: '吉他', code: 'G'},
            playCategoryCode: 'TC',
            playCategorySelect: {name: '弹唱', code: 'TC'},
          },

        ]
      },

      editItem(item) {
        this.editedIndex = this.notations.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialog = true
      },

      deleteItem(item) {
        this.editedIndex = this.notations.indexOf(item)
        this.editedItem = Object.assign({}, item)
        this.dialogDelete = true
      },

      deleteItemConfirm() {
        this.notations.splice(this.editedIndex, 1)
        this.closeDelete()
      },

      onPopUp() {
        var that = this
        var el = document.getElementById('fileChips')
        // 常用
        new Sortable(el, {
          onEnd: function (evt) {
            // 获取排序之后的data数据
            that.files.splice(evt.newIndex, 0, that.files.splice(evt.oldIndex, 1)[0])
            var newArray = that.files.slice(0)
            that.files = []
            that.$nextTick(function () {
              that.files = newArray
              console.log('heree' + that.files)
            })
          }
        })
      },

      close() {
        this.dialog = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.files = []
          this.thumbnailUrls = []
          this.editedIndex = -1
        })
      },

      closeDelete() {
        this.dialogDelete = false
        this.$nextTick(() => {
          this.editedItem = Object.assign({}, this.defaultItem)
          this.editedIndex = -1
        })
      },

      save() {
        // 这是后台返回的 code，前台需要做个转换，转成 select
        this.editedItem.notationCategoryCode = this.editedItem.notationCategorySelect.code;
        this.editedItem.playCategoryCode = this.editedItem.playCategorySelect.code;

        if (this.editedIndex > -1) {
          // 编辑操作
          Object.assign(this.notations[this.editedIndex], this.editedItem)
        } else {
          // 新增操作
          // this.notations.push(this.editedItem)
          this.saveNotation(this.editedItem);
        }
        this.close()
      },

      onUploadFile(files) {

        if (files && files.length > 0) {
          // 生成缩略图
          this.generateThumbnail(files);

          // 组装上传参数
          let formData = new FormData();
          for (let i = 0; i < files.length; i++) {
            formData.append("files", files[i]);
          }

          // 上传到系统的临时路径
          this.$axios.post('/file/upload', formData).then((response) => {
            const {fileUrls} = response.data;
            this.editedItem.notationUrls = fileUrls;
            // for (let i = 0; i < fileUrls.length; i++) {
            //   this.notationUrls.push(fileUrls[i]);
            // }
            console.log(this.notationUrls)
          });

        } else {
          console.log('image is null');
        }

      },

      generateThumbnail(files) {
        // 生成缩略图
        for (let i = 0; i < files.length; i++) {
          let url = URL.createObjectURL(files[i]);
          this.thumbnailUrls.push(url);
        }

        this.files = files;

        var that = this
        var el = document.getElementById('thumbnailDiv')
        // 常用
        new Sortable(el, {
          onEnd: function (evt) {
            // 获取排序之后的data数据
            that.files.splice(evt.newIndex, 0, that.files.splice(evt.oldIndex, 1)[0])
            var newArray = that.files.slice(0)
            that.files = []
            that.$nextTick(function () {
              that.files = newArray
              console.log('heree' + that.files)
            })
          }
        })
      },


      saveNotation(param) {
        let notationDTO = {
          'notationName': param.notationName,
          'author': param.author,
          'songName': param.songName,
          'artist': param.artist,
          'notationCategoryCode': param.notationCategoryCode,
          'playCategoryCode': param.playCategoryCode,
          'notationUrls': param.notationUrls,
        }

        console.log(notationDTO);

        this.$axios.post('/notation', notationDTO).then((response) => {
          console.log(response.data);
          // this.$emit('notationSubmitted');
          this.queryAllNotations();
        });
      },

      queryAllNotations() {
        this.loading = true;

        const {sortBy, sortDesc, page, itemsPerPage} = this.options

        let param = {};
        param.page = page;
        param.size = itemsPerPage;
        param.sortBy = sortBy;
        param.sortDesc = sortDesc;

        this.$axios.get("/notations", {params: param}).then((response) => {
          const {rows, total} = response.data;

          if (rows) {
            // this.notations = notations.map(this.getDisplayNotations);
            this.notations = rows;
            this.total = total;
            this.loading = false;
          }
        });
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
    },


    filters: {
      getNotionCategory(code) {
        if (code === 'G') {
          return '吉他';
        } else if (code === 'U') {
          return '尤克里里'
        }
        return '';
      },
      getPlayCategory(code) {
        if (code === 'TC') {
          return '弹唱';
        } else if (code === 'ZT') {
          return '指弹'
        }
        return '';
      },
    },

    mounted() {
      this.queryAllNotations();
    },


  }
</script>