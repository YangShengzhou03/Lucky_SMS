<template>
  <div class="modern-chat-app">
    <div class="sidebar">
      <div class="sidebar-header">
        <h2>消息</h2>
        <div class="header-actions">
          <el-dropdown trigger="click" @command="handlePlusMenuCommand">
            <el-button type="text" icon="Plus" circle />
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="addContact">
                  <el-icon><User /></el-icon>
                  添加好友
                </el-dropdown-item>
                <el-dropdown-item command="createGroup">
                  <el-icon><UserFilled /></el-icon>
                  创建群组
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <div class="search-box">
        <el-input 
          v-model="searchQuery" 
          placeholder="搜索对话" 
          clearable 
          :prefix-icon="Search" 
          @input="handleSearchInput"
        />
      </div>

      <div class="chat-list" ref="chatListRef">
        <div v-if="filteredChats.length > 0" class="virtual-scroller">
          <div
            v-for="(chat, index) in filteredChats"
            :key="index"
            :class="['chat-item', { active: currentChatIndex === index }]"
            @click="selectChat(index)"
          >
            <el-avatar :size="48" :src="chat.avatar" :alt="chat.name">
              <template v-if="chat.isGroup && !chat.avatar">
                <el-icon><User /></el-icon>
              </template>
            </el-avatar>
            <div class="chat-info">
              <div class="chat-header">
                <h3>
                  {{ chat.name }}
                  <span v-if="chat.isGroup" class="group-badge">群</span>
                </h3>
                <span>{{ formatRelativeTime(chat.lastMessageTime) }}</span>
              </div>
              <p class="last-message">
                <template v-if="chat.lastMessageType === 'image'">
                  <el-icon><Picture /></el-icon> 图片
                </template>
                <template v-if="chat.lastMessageType === 'file'">
                  <el-icon><Document /></el-icon> 文件
                </template>
                <template v-else>{{ chat.preview }}</template>
              </p>
            </div>
            <el-badge 
              :value="chat.unreadCount" 
              :max="99" 
              :hidden="!chat.unreadCount" 
              type="primary" 
            />
          </div>
        </div>
        
        <div v-if="filteredChats.length === 0 && searchQuery" class="empty-search">
          <el-empty description="未找到相关对话" />
        </div>
      </div>
    </div>

    <div class="chat-area">
      <div v-if="!currentChat" class="empty-chat">
        <div class="empty-content">
          <el-icon><ChatRound /></el-icon>
          <h3>选择一个对话开始聊天</h3>
          <p>或创建一个新的对话</p>
          <el-dropdown trigger="click" @command="handlePlusMenuCommand">
            <el-button type="primary">
              <el-icon><Plus /></el-icon>
              新建对话
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="addContact">
                  <el-icon><User /></el-icon>
                  添加好友
                </el-dropdown-item>
                <el-dropdown-item command="createGroup">
                  <el-icon><UserFilled /></el-icon>
                  创建群组
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <div v-else class="active-chat">
        <div class="chat-header">
          <div class="header-left">
            <el-avatar :size="40" :src="currentChat.avatar" :alt="currentChat.name">
              <template v-if="currentChat.isGroup && !currentChat.avatar">
                <el-icon><User /></el-icon>
              </template>
            </el-avatar>
            <div class="user-info">
              <h3>
                {{ currentChat.name }}
                <span v-if="currentChat.isGroup" class="group-badge">群</span>
                <span v-if="currentChat.isGroup" class="member-count">{{ currentChat.members.length }}人</span>
              </h3>
              <p v-if="currentChat.online" class="online-status">
                <span class="online-dot"></span> 在线
              </p>
              <p v-else class="online-status">
                上次在线 {{ formatRelativeTime(currentChat.lastOnline) }}
              </p>
            </div>
          </div>
          <div class="header-actions">
            <el-button type="text" icon="Phone" circle v-if="!currentChat.isGroup" />
            <el-button type="text" icon="VideoCamera" circle v-if="!currentChat.isGroup" />
            <el-button type="text" icon="User" circle v-if="currentChat.isGroup" @click="showAddMembersDialog = true" />
            <el-dropdown>
              <el-button type="text" icon="More" circle />
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="clearChatHistory">
                    <el-icon><Delete /></el-icon> 清空聊天记录
                  </el-dropdown-item>
                  <el-dropdown-item @click="muteChat">
                    <el-icon><BellFilled /></el-icon> 静音
                  </el-dropdown-item>
                  <el-dropdown-item v-if="currentChat.isGroup" @click="showGroupMembersDialog = true">
                    <el-icon><User /></el-icon> 群成员管理
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>

        <div class="messages-container">
          <div class="messages" ref="messagesContainer">
            <div v-if="isLoadingMessages" class="loading-messages">
              <el-icon class="is-loading"><Loading /></el-icon>
              <span>加载消息中...</span>
            </div>
            
            <template v-else>
              <div class="message-group" v-for="(group, index) in messageGroups" :key="index">
                <div class="date-divider">
                  <span>{{ formatMessageDate(group.date) }}</span>
                </div>

                <div v-for="message in group.messages" :key="message.id" :class="['message', message.type]">
                  <el-avatar v-if="message.type === 'bot'" :size="36" :src="getSenderAvatar(message)" />

                  <div class="message-content">
                    <div v-if="currentChat.isGroup && message.type === 'bot'" class="sender-name">
                      {{ getSenderName(message) }}
                    </div>
                    <div class="message-bubble">
                      <img 
                        v-if="message.contentType === 'image'" 
                        :src="message.content" 
                        @click="previewImage(message.content)"
                        loading="lazy"
                      />
                      <div v-else-if="message.contentType === 'file'" class="file-message">
                        <el-icon><Document /></el-icon>
                        <div class="file-info">
                          <div class="file-name">{{ getFileName(message.content) }}</div>
                          <div class="file-size">{{ message.fileSize }}</div>
                        </div>
                        <el-button type="primary" text @click="downloadFile(message.content)">下载</el-button>
                      </div>
                      <div v-else v-html="formatMessage(message.content)"></div>
                    </div>

                    <div class="message-meta">
                      <span>{{ formatMessageTime(message.time) }}</span>
                      <el-icon v-if="message.type === 'user'" :class="['status-icon', message.status]">
                        <template v-if="message.status === 'sending'"><Loading /></template>
                        <template v-if="message.status === 'sent'"><CircleCheck /></template>
                        <template v-if="message.status === 'read'"><Select /></template>
                        <template v-if="message.status === 'failed'"><CircleClose /></template>
                      </el-icon>
                    </div>
                  </div>

                  <el-dropdown v-if="message.type === 'user'" trigger="click" @command="handleMessageCommand($event, message)">
                    <el-button type="text" icon="More" class="message-actions" />
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="copy">复制</el-dropdown-item>
                        <el-dropdown-item command="recall" :disabled="message.status === 'sending'">撤回</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
              
              <div v-if="isLoadingMoreMessages" class="loading-more">
                <el-icon class="is-loading"><Loading /></el-icon>
                <span>加载更多消息...</span>
              </div>
            </template>
          </div>
        </div>

        <div class="input-area">
          <div class="input-tools">
            <el-button type="text" icon="Picture" @click="triggerFileInput('image')" />
            <el-button type="text" icon="VideoCamera" @click="triggerFileInput('video')" />
            <el-button type="text" icon="Document" @click="triggerFileInput('file')" />
            <el-button type="text" icon="Emoji" @click="showEmojiPicker = !showEmojiPicker" />
            <input type="file" ref="fileInput" style="display: none" @change="handleFileUpload" />
          </div>

          <div class="input-wrapper">
            <el-input 
              v-model="newMessage" 
              type="textarea" 
              :rows="2" 
              :maxlength="500" 
              placeholder="输入消息..."
              resize="none" 
              @keydown.enter.exact.prevent="sendMessage"
              @keydown.shift.enter.prevent="addNewLine"
              class="embedded-input"
            />
            <div class="send-button-container">
              <el-button 
                class="send-button" 
                type="primary" 
                :icon="Promotion" 
                circle
                :disabled="!canSendMessage" 
                @click="sendMessage" 
                :loading="sending" 
              />
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 添加联系人对话框 -->
    <el-dialog 
      v-model="showNewChatDialog" 
      title="添加联系人" 
      width="520px" 
      append-to-body
      class="custom-dialog new-chat-dialog" 
      :before-close="handleCloseNewChatDialog" 
      :close-on-click-modal="false"
    >
      <template #title>
        <div class="dialog-title">
          <span class="dialog-title-text">添加联系人</span>
        </div>
      </template>

      <div class="dialog-content">
        <!-- 选项卡切换 -->
        <div class="dialog-tabs">
          <div 
            class="tab-item"
            :class="{ active: activeTab === 'search' }"
            @click="activeTab = 'search'"
          >
            <el-icon><Search /></el-icon>
            <span>搜索添加</span>
          </div>
          <div 
            class="tab-item"
            :class="{ active: activeTab === 'qr' }"
            @click="activeTab = 'qr'"
          >
            <el-icon><Grid /></el-icon>
            <span>扫码添加</span>
          </div>
          <div 
            class="tab-item"
            :class="{ active: activeTab === 'contacts' }"
            @click="activeTab = 'contacts'"
          >
            <el-icon><User /></el-icon>
            <span>手机联系人</span>
          </div>
        </div>

        <!-- 搜索添加选项卡 -->
        <div v-if="activeTab === 'search'" class="tab-content">
          <div class="search-section">
            <div class="search-input-wrapper">
              <el-input 
                v-model="newChatSearch" 
                placeholder="输入姓名、手机号或邮箱搜索" 
                :prefix-icon="Search" 
                class="modern-search-input"
                @input="onSearchInput" 
                clearable 
                @clear="clearSearch" 
                :loading="isSearchingContacts"
              />
            </div>
          </div>

          <div class="contact-list-section">
            <div v-if="!searchPerformed" class="empty-state">
              <div class="empty-icon">
                <el-icon><Search /></el-icon>
              </div>
              <h3 class="empty-title">搜索联系人</h3>
              <p class="empty-desc">输入姓名、手机号或邮箱开始搜索</p>
              <el-button type="primary" text class="create-contact-btn">
                <el-icon><Plus /></el-icon>
                创建新联系人
              </el-button>
            </div>

            <div v-else-if="isSearchingContacts" class="loading-state">
              <div class="skeleton-list">
                <div v-for="i in 3" :key="i" class="skeleton-item">
                  <div class="skeleton-avatar"></div>
                  <div class="skeleton-content">
                    <div class="skeleton-name"></div>
                    <div class="skeleton-desc"></div>
                  </div>
                </div>
              </div>
            </div>

            <div v-else-if="filteredContacts.length > 0" class="contact-list">
              <div v-for="contact in filteredContacts" :key="contact.id" class="modern-contact-card" @click="createNewChat(contact)">
                <div class="contact-avatar">
                  <el-avatar :size="48" :src="contact.avatar" :alt="contact.name" />
                  <div class="online-indicator" :class="{ online: contact.online }"></div>
                </div>
                <div class="contact-info">
                  <div class="contact-header">
                    <h3 class="contact-name">
                      <span v-html="highlightMatch(contact.name, newChatSearch)"></span>
                    </h3>
                    <el-button 
                      type="primary" 
                      size="small" 
                      class="add-contact-btn"
                      @click.stop="createNewChat(contact)"
                    >
                      添加
                    </el-button>
                  </div>
                  <div class="contact-details">
                    <span class="online-status" :class="{ online: contact.online }">
                      {{ contact.online ? '在线' : '离线' }}
                    </span>
                    <span class="department">{{ contact.department }}</span>
                  </div>
                </div>
              </div>
            </div>

            <div v-else-if="newChatSearch.trim()" class="empty-result">
              <div class="empty-icon">
                <el-icon><User /></el-icon>
              </div>
              <h3 class="empty-title">未找到联系人</h3>
              <p class="empty-desc">尝试使用不同的关键词或添加新联系人</p>
              <el-button type="primary" text class="create-contact-btn">
                <el-icon><Plus /></el-icon>
                创建新联系人
              </el-button>
            </div>
          </div>
        </div>

        <!-- 扫码添加选项卡 -->
        <div v-if="activeTab === 'qr'" class="tab-content">
          <div class="qr-section">
            <div class="qr-container">
              <div class="qr-placeholder">
                <el-icon><Grid /></el-icon>
                <p>二维码功能开发中</p>
              </div>
            </div>
            <p class="qr-tip">扫描对方的二维码名片快速添加</p>
          </div>
        </div>

        <!-- 手机联系人选项卡 -->
        <div v-if="activeTab === 'contacts'" class="tab-content">
          <div class="contacts-section">
            <div class="contacts-placeholder">
              <el-icon><Phone /></el-icon>
              <p>手机联系人功能开发中</p>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showNewChatDialog = false" class="cancel-btn">
            取消
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 创建群组对话框 -->
    <el-dialog 
      v-model="showCreateGroupDialog" 
      title="创建群组" 
      width="520px" 
      append-to-body
      class="custom-dialog create-group-dialog" 
      :close-on-click-modal="false"
    >
      <template #title>
        <div class="dialog-title">
          <span class="dialog-title-text">创建群组</span>
        </div>
      </template>

      <div class="dialog-content">
        <el-form :model="groupForm" ref="groupFormRef" class="modern-group-form">
          <div class="form-section">
            <div class="form-item">
              <label class="form-label">群名称</label>
              <div class="input-wrapper">
                <el-input 
                  v-model="groupForm.name" 
                  placeholder="请输入群组名称（3-20字）" 
                  maxlength="20" 
                  show-word-limit
                  class="modern-input"
                />
                <div class="input-tip">群名称不能为空</div>
              </div>
            </div>
            
            <div class="form-item">
              <label class="form-label">群描述</label>
              <div class="input-wrapper">
                <el-input 
                  v-model="groupForm.description" 
                  type="textarea" 
                  :rows="3"
                  placeholder="请输入群组描述（可选）" 
                  maxlength="200" 
                  show-word-limit
                  class="modern-textarea"
                />
              </div>
            </div>

            <div class="form-item">
              <label class="form-label">群头像</label>
              <div class="avatar-upload-section">
                <el-upload
                  class="modern-avatar-uploader"
                  action="#"
                  :show-file-list="false"
                  :http-request="handleAvatarUpload"
                  :before-upload="beforeAvatarUpload"
                  drag
                >
                  <div class="avatar-upload-area">
                    <el-avatar :size="80" :src="groupForm.avatar" class="upload-avatar">
                      <el-icon><Plus /></el-icon>
                    </el-avatar>
                    <div class="upload-tip">
                      <p>点击或拖拽上传群头像</p>
                      <p class="upload-hint">支持 JPG、PNG 格式，大小不超过 2MB</p>
                    </div>
                  </div>
                </el-upload>
              </div>
            </div>
          </div>

          <div class="form-section">
            <div class="form-item">
              <label class="form-label">群成员</label>
              <div class="members-section">
                <div class="members-search">
                  <el-input 
                    v-model="groupMemberSearch" 
                    placeholder="搜索联系人" 
                    :prefix-icon="Search" 
                    class="modern-search-input"
                    clearable
                    @input="debouncedFilterGroupMembers"
                  />
                </div>
                <div class="members-list">
                  <div v-for="member in filteredGroupMembers" :key="member.id" 
                    class="modern-member-item"
                    :class="{ 'selected': isMemberSelected(member.id) }"
                    @click="toggleMemberSelection(member.id)"
                  >
                    <div class="member-avatar">
                      <el-avatar :size="40" :src="member.avatar" />
                      <div class="online-indicator" :class="{ online: member.online }"></div>
                    </div>
                    <div class="member-info">
                      <span class="member-name">{{ member.name }}</span>
                      <span class="member-desc">{{ member.department }}</span>
                    </div>
                    <div class="member-action">
                      <el-icon v-if="isMemberSelected(member.id)" class="selected-icon"><Check /></el-icon>
                    </div>
                  </div>
                </div>
                <div class="members-summary">
                  <span class="selected-count">已选择 {{ groupForm.members.length }} 人</span>
                  <span class="min-requirement" v-if="groupForm.members.length === 0">至少选择一位群成员</span>
                </div>
              </div>
            </div>
          </div>
        </el-form>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelCreateGroup" class="cancel-btn">
            取消
          </el-button>
          <el-button 
            type="primary" 
            @click="createGroup" 
            :loading="isCreatingGroup" 
            :disabled="!canCreateGroup"
            class="create-btn"
          >
            创建群组
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 群成员管理对话框 -->
    <el-dialog 
      v-model="showGroupMembersDialog" 
      title="群成员管理" 
      width="520px" 
      append-to-body
      class="custom-dialog group-members-dialog" 
      :close-on-click-modal="false"
    >
      <template #title>
        <div class="dialog-title">
          <span class="dialog-title-text">群成员管理</span>
        </div>
      </template>

      <div class="dialog-content">
        <div class="members-management-section">
          <div class="members-header">
            <div class="header-info">
              <h3 class="members-section-title">群成员列表</h3>
              <p class="section-desc">管理群成员权限和状态</p>
            </div>
            <div class="header-actions">
                          <el-button 
              type="primary" 
              size="small" 
              @click="showAddMembersDialog = true"
              class="add-members-btn"
            >
              <el-icon><User /></el-icon>
              添加成员
            </el-button>
            </div>
          </div>

          <div class="members-list-container">
            <div v-if="currentChat?.members.length === 0" class="empty-members">
              <div class="empty-icon">
                <el-icon><User /></el-icon>
              </div>
              <h3 class="empty-title">暂无群成员</h3>
              <p class="empty-desc">点击上方按钮添加群成员</p>
            </div>
            
            <div v-else class="modern-members-list">
              <div v-for="member in currentChat?.members || []" :key="member.id" class="modern-member-card">
                <div class="member-avatar">
                  <el-avatar :size="48" :src="member.avatar" />
                  <div class="online-indicator" :class="{ online: member.online }"></div>
                </div>
                <div class="member-info">
                  <div class="member-header">
                    <h4 class="member-name">
                      {{ member.name }}
                      <span v-if="member.isAdmin" class="role-badge owner">群主</span>
                      <span v-else-if="member.role === 'ADMIN'" class="role-badge admin">管理员</span>
                      <span v-else class="role-badge member">成员</span>
                    </h4>
                    <div class="member-actions">
                      <el-dropdown v-if="canManageMember(member)" @command="handleMemberCommand($event, member)">
                        <el-button type="text" icon="More" class="member-action-btn" />
                        <template #dropdown>
                          <el-dropdown-menu>
                            <el-dropdown-item command="remove">
                              <el-icon><Delete /></el-icon>
                              移除成员
                            </el-dropdown-item>
                            <el-dropdown-item 
                              command="toggleAdmin" 
                              v-if="!member.isAdmin && isCurrentUserAdmin()"
                            >
                              <el-icon><UserFilled /></el-icon>
                              {{ member.role === 'ADMIN' ? '取消管理员' : '设为管理员' }}
                            </el-dropdown-item>
                          </el-dropdown-menu>
                        </template>
                      </el-dropdown>
                    </div>
                  </div>
                  <div class="member-details">
                    <span class="online-status" :class="{ online: member.online }">
                      {{ member.online ? '在线' : `上次在线 ${formatRelativeTime(member.lastOnline)}` }}
                    </span>
                    <span class="join-time">加入时间：{{ formatRelativeTime(member.joinTime || new Date()) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showGroupMembersDialog = false" class="cancel-btn">
            关闭
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 添加群成员对话框 -->
    <el-dialog 
      v-model="showAddMembersDialog" 
      title="添加群成员" 
      width="520px" 
      append-to-body
      class="custom-dialog add-members-dialog" 
      :close-on-click-modal="false"
    >
      <template #title>
        <div class="dialog-title">
          <span class="dialog-title-text">添加群成员</span>
          <span class="add-count-badge" v-if="membersToAdd.length > 0">已选择 {{ membersToAdd.length }} 人</span>
        </div>
      </template>

      <div class="dialog-content">
        <div class="add-members-section">
          <div class="search-section">
            <div class="search-input-wrapper">
              <el-input 
                v-model="addMemberSearch" 
                placeholder="搜索联系人" 
                :prefix-icon="Search" 
                class="modern-search-input"
                clearable
                @input="debouncedFilterAddMembers"
              />
            </div>
          </div>

          <div class="members-selection-container">
            <div v-if="filteredAddMembers.length === 0 && addMemberSearch.trim()" class="empty-search-result">
              <div class="empty-icon">
                <el-icon><User /></el-icon>
              </div>
              <h3 class="empty-title">未找到联系人</h3>
              <p class="empty-desc">尝试使用不同的关键词搜索</p>
            </div>
            
            <div v-else-if="filteredAddMembers.length === 0" class="empty-contacts">
              <div class="empty-icon">
                <el-icon><User /></el-icon>
              </div>
              <h3 class="empty-title">暂无可用联系人</h3>
              <p class="empty-desc">所有联系人已在群组中</p>
            </div>
            
            <div v-else class="modern-add-members-list">
              <div v-for="member in filteredAddMembers" :key="member.id" 
                class="modern-add-member-card"
                :class="{ 
                  'selected': isMemberInGroup(member.id),
                  'disabled': isMemberInGroup(member.id)
                }"
                @click="!isMemberInGroup(member.id) && addMemberToGroup(member)"
              >
                <div class="member-avatar">
                  <el-avatar :size="48" :src="member.avatar" />
                  <div class="online-indicator" :class="{ online: member.online }"></div>
                </div>
                <div class="member-info">
                  <div class="member-header">
                    <h4 class="member-name">{{ member.name }}</h4>
                    <div class="member-action">
                      <el-icon v-if="isMemberInGroup(member.id)" class="selected-icon"><Check /></el-icon>
                      <el-icon v-else class="add-icon"><Plus /></el-icon>
                    </div>
                  </div>
                  <div class="member-details">
                    <span class="online-status" :class="{ online: member.online }">
                      {{ member.online ? '在线' : '离线' }}
                    </span>
                    <span class="department">{{ member.department }}</span>
                  </div>
                </div>
                <div class="selection-overlay" v-if="isMemberInGroup(member.id)">
                  <span class="already-in-group">已在群组中</span>
                </div>
              </div>
            </div>
          </div>

          <div v-if="membersToAdd.length > 0" class="selected-members-preview">
            <div class="preview-header">
              <h4 class="preview-title">已选择的成员</h4>
              <el-button type="text" @click="membersToAdd = []" class="clear-selection-btn">
                清空选择
              </el-button>
            </div>
            <div class="selected-members-list">
              <div v-for="member in membersToAdd" :key="member.id" class="selected-member-tag">
                <el-avatar :size="24" :src="member.avatar" />
                <span class="member-name">{{ member.name }}</span>
                <el-button 
                  type="text" 
                  icon="Delete" 
                  size="small" 
                  @click="removeFromSelection(member.id)"
                  class="remove-btn"
                />
              </div>
            </div>
          </div>
        </div>
      </div>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancelAddMembers" class="cancel-btn">
            取消
          </el-button>
          <el-button 
            type="primary" 
            @click="confirmAddMembers" 
            :loading="isAddingMembers" 
            :disabled="membersToAdd.length === 0"
            class="confirm-btn"
          >
            确认添加 ({{ membersToAdd.length }})
          </el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 图片预览对话框 -->
    <el-dialog v-model="imagePreviewVisible" title="图片预览" width="80%" class="image-preview-dialog">
      <div class="image-preview-container">
        <img :src="previewImageUrl" class="preview-image" />
        <div class="image-preview-actions">
          <el-button @click="downloadImage(previewImageUrl)" type="primary">
            <el-icon><Download /></el-icon> 下载图片
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, nextTick, reactive, onMounted, watch } from 'vue'
import {
  Search,
  Picture,
  Document,
  Promotion,
  Loading,
  CircleCheck,
  Select,
  CircleClose,
  Delete,
  BellFilled,
  ChatRound,
  User,
  UserFilled,
  Check,
  Plus,
  Download,
  Grid,
  Phone
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useClipboard } from '@vueuse/core'
import { debounce } from 'lodash-es'

const { copy } = useClipboard()


const currentUser = ref({
  id: 100,
  name: '我',
  avatar: 'https://randomuser.me/api/portraits/men/1.jpg'
})


const contacts = ref([
  { id: 1, name: '张教授', avatar: 'https://randomuser.me/api/portraits/men/32.jpg', online: true, department: '计算机学院' },
  { id: 2, name: '李老师', avatar: 'https://randomuser.me/api/portraits/women/44.jpg', online: false, lastOnline: new Date(Date.now() - 3600000), department: '数学系' },
  { id: 3, name: '王老师', avatar: 'https://randomuser.me/api/portraits/men/45.jpg', online: true, department: '计算机学院' },
  { id: 4, name: '赵老师', avatar: 'https://randomuser.me/api/portraits/women/68.jpg', online: true, department: '外语系' },
  { id: 5, name: '教务处', avatar: 'https://randomuser.me/api/portraits/women/68.jpg', online: false, lastOnline: new Date(Date.now() - 86400000), department: '行政部门' },
  { id: 6, name: '学生事务办公室', avatar: 'https://randomuser.me/api/portraits/lego/3.jpg', online: true, department: '行政部门' },
])


const chatHistory = ref([
  {
    id: 1,
    name: '张教授',
    avatar: 'https://randomuser.me/api/portraits/men/32.jpg',
    lastMessageTime: new Date(),
    lastMessageType: 'text',
    preview: '关于下周的研讨会，你有什么想法？',
    unreadCount: 0,
    online: true,
    department: '计算机学院',
    isGroup: false,
    messages: [
      {
        id: 101,
        type: 'bot',
        senderId: 1,
        contentType: 'text',
        content: '你好！关于下周的研讨会，你有什么想法？',
        time: new Date(Date.now() - 3600000 * 2),
        status: 'read'
      },
      {
        id: 102,
        type: 'user',
        senderId: 100,
        contentType: 'text',
        content: '我认为可以重点讨论一下人工智能在教育中的应用',
        time: new Date(Date.now() - 3600000 * 1.8),
        status: 'read'
      },
      {
        id: 103,
        type: 'bot',
        senderId: 1,
        contentType: 'text',
        content: '这是个很好的主题！我们可以准备以下几个方面的内容:<br>1. AI辅助教学系统<br>2. 智能评测系统<br>3. 个性化学习路径',
        time: new Date(Date.now() - 3600000 * 1.5),
        status: 'read'
      }
    ]
  },
  {
    id: 2,
    name: '李老师',
    avatar: 'https://randomuser.me/api/portraits/women/44.jpg',
    lastMessageTime: new Date(Date.now() - 86400000),
    lastMessageType: 'file',
    preview: '这是本学期教学大纲，请查收',
    unreadCount: 3,
    online: false,
    lastOnline: new Date(Date.now() - 3600000),
    department: '数学系',
    isGroup: false,
    messages: [
      {
        id: 201,
        type: 'bot',
        senderId: 2,
        contentType: 'text',
        content: '这是本学期教学大纲，请查收',
        time: new Date(Date.now() - 86400000),
        status: 'read'
      },
      {
        id: 202,
        type: 'bot',
        senderId: 2,
        contentType: 'file',
        content: 'https://example.com/files/syllabus.pdf',
        fileSize: '2.1MB',
        time: new Date(Date.now() - 86400000),
        status: 'read'
      }
    ]
  },
  {
    id: 3,
    name: '计算机学院',
    avatar: 'https://randomuser.me/api/portraits/lego/3.jpg',
    lastMessageTime: new Date(Date.now() - 86400000 * 2),
    lastMessageType: 'text',
    preview: '提醒：教师培训工作坊将于本周五举行',
    unreadCount: 0,
    online: true,
    department: '行政部门',
    isGroup: true,
    members: [
      { id: 100, name: '我', avatar: 'https://randomuser.me/api/portraits/men/1.jpg', role: 'MEMBER', online: true, isAdmin: false },
      { id: 5, name: '教务处', avatar: 'https://randomuser.me/api/portraits/women/68.jpg', role: 'ADMIN', online: false, lastOnline: new Date(Date.now() - 86400000), isAdmin: true },
      { id: 1, name: '张教授', avatar: 'https://randomuser.me/api/portraits/men/32.jpg', role: 'MEMBER', online: true, isAdmin: false },
      { id: 3, name: '王老师', avatar: 'https://randomuser.me/api/portraits/men/45.jpg', role: 'MEMBER', online: true, isAdmin: false }
    ],
    messages: [
      {
        id: 301,
        type: 'bot',
        senderId: 5,
        contentType: 'text',
        content: '提醒：教师培训工作坊将于本周五下午2点在A101举行',
        time: new Date(Date.now() - 86400000 * 2),
        status: 'read'
      },
      {
        id: 302,
        type: 'bot',
        senderId: 5,
        contentType: 'text',
        content: '主题：创新教学方法与技术应用',
        time: new Date(Date.now() - 86400000 * 2),
        status: 'read'
      }
    ]
  }
])


const searchQuery = ref('')
const newChatSearch = ref('')
const searchPerformed = ref(false)
const currentChatIndex = ref(0)
const newMessage = ref('')
const sending = ref(false)
const fileInput = ref(null)
const fileToUpload = ref(null)
const uploadType = ref('file')
const imagePreviewVisible = ref(false)
const previewImageUrl = ref('')
const showNewChatDialog = ref(false)
const messagesContainer = ref(null)
const chatListRef = ref(null)
const showEmojiPicker = ref(false)
const isSearchingContacts = ref(false)
const isLoadingMessages = ref(false)
const isLoadingMoreMessages = ref(false)
const isCreatingGroup = ref(false)
const isAddingMembers = ref(false)


const activeTab = ref('search')


const showCreateGroupDialog = ref(false)
const showGroupMembersDialog = ref(false)
const showAddMembersDialog = ref(false)
const groupForm = reactive({
  name: '',
  description: '',
  avatar: '',
  members: []
})
const groupMemberSearch = ref('')
const filteredGroupMembers = ref([...contacts.value])
const groupFormRef = ref(null)
const addMemberSearch = ref('')
const filteredAddMembers = ref([...contacts.value])
const membersToAdd = ref([])


const filteredChats = computed(() => {
  if (!searchQuery.value.trim()) return chatHistory.value
  return chatHistory.value.filter(chat =>
    chat.name.toLowerCase().includes(searchQuery.value.toLowerCase())
  )
})

const filteredContacts = computed(() => {
  if (!newChatSearch.value.trim()) return []
  return contacts.value.filter(contact =>
    contact.name.toLowerCase().includes(newChatSearch.value.toLowerCase())
  )
})

const currentChat = computed(() => {
  if (currentChatIndex.value === null || !chatHistory.value.length) return null
  return chatHistory.value[currentChatIndex.value]
})

const messageGroups = computed(() => {
  if (!currentChat.value) return []

  const groups = []
  let currentGroup = null

  currentChat.value.messages.forEach(message => {
    const messageDate = new Date(message.time).toDateString()

    if (!currentGroup || currentGroup.date !== messageDate) {
      currentGroup = {
        date: messageDate,
        messages: []
      }
      groups.push(currentGroup)
    }

    currentGroup.messages.push(message)
  })

  return groups
})

const canSendMessage = computed(() => {
  return (newMessage.value.trim() || fileToUpload.value) && !sending.value
})

const canCreateGroup = computed(() => {
  return groupForm.name.trim() && groupForm.members.length > 0
})


const selectChat = (index) => {
  currentChatIndex.value = index
  if (currentChat.value?.unreadCount > 0) {
    currentChat.value.unreadCount = 0
  }
  loadChatMessages()
}

const loadChatMessages = async () => {
  if (!currentChat.value) return
  
  isLoadingMessages.value = true
  
  try {
    
    await new Promise(resolve => setTimeout(resolve, 300))
    
    nextTick(() => {
      scrollToBottom()
    })
  } catch (error) {
    console.error('加载消息失败:', error)
    ElMessage.error('加载消息失败')
  } finally {
    isLoadingMessages.value = false
  }
}

const handleSearchInput = debounce(() => {
  
}, 300)

const formatMessage = (content) => {
  if (!content) return ''
  const urlRegex = /(https?:\/\/[^\s]+)/g
  return content.replace(urlRegex, url => {
    return `<a href="${url}" target="_blank" class="message-link">${url}</a>`
  })
}

const formatRelativeTime = (date) => {
  if (!date) return ''

  const now = new Date()
  const diffInSeconds = Math.floor((now - new Date(date)) / 1000)

  if (diffInSeconds < 60) return '刚刚'
  if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}分钟前`
  if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)}小时前`
  if (diffInSeconds < 604800) return `${Math.floor(diffInSeconds / 86400)}天前`

  return new Date(date).toLocaleDateString()
}

const formatMessageTime = (date) => {
  if (!date) return ''
  return new Date(date).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

const formatMessageDate = (dateString) => {
  const date = new Date(dateString)
  const today = new Date()
  const yesterday = new Date(today)
  yesterday.setDate(yesterday.getDate() - 1)

  if (date.toDateString() === today.toDateString()) return '今天'
  if (date.toDateString() === yesterday.toDateString()) return '昨天'

  return date.toLocaleDateString([], { month: 'short', day: 'numeric' })
}

const sendMessage = async () => {
  if (!canSendMessage.value || !currentChat.value) return

  sending.value = true

  try {
    let messageContent = newMessage.value.trim()
    let contentType = 'text'
    let fileSize = null

    if (fileToUpload.value) {
      showUploadProgress()
      await new Promise(resolve => setTimeout(resolve, 800))

      if (uploadType.value === 'image') {
        contentType = 'image'
        messageContent = URL.createObjectURL(fileToUpload.value)
      } else {
        contentType = 'file'
        messageContent = `https://example.com/files/${fileToUpload.value.name}`
        fileSize = formatFileSize(fileToUpload.value.size)
      }
    }

    const message = {
      id: Date.now(),
      type: 'user',
      senderId: currentUser.value.id,
      contentType,
      content: messageContent,
      fileSize,
      time: new Date(),
      status: 'sending'
    }

    currentChat.value.messages.push(message)
    updateChatPreview(currentChat.value, message)
    newMessage.value = ''
    fileToUpload.value = null

    nextTick(() => {
      scrollToBottom()
    })

    setTimeout(() => {
      message.status = 'sent'

      
      if (!currentChat.value.isGroup && currentChat.value.id === 1) {
        setTimeout(() => {
          const replies = [
            "好的，我会把这些建议纳入研讨会讨论。",
            "这个方向很有价值，我们可以深入探讨。",
            "感谢你的建议，我会准备相关资料。"
          ]

          const botMessage = {
            id: Date.now(),
            type: 'bot',
            senderId: 1,
            contentType: 'text',
            content: replies[Math.floor(Math.random() * replies.length)],
            time: new Date(),
            status: 'read'
          }

          currentChat.value.messages.push(botMessage)
          updateChatPreview(currentChat.value, botMessage)

          nextTick(() => {
            scrollToBottom()
          })
        }, 1500)
      }
    }, 800)
  } catch (error) {
    console.error('发送消息失败:', error)
    ElMessage.error('发送消息失败')
  } finally {
    sending.value = false
  }
}

const addNewLine = () => {
  newMessage.value += '\n'
}

// const insertEmoji = (emoji) => {
//   newMessage.value += emoji
//   showEmojiPicker.value = false
// }

const showUploadProgress = () => {
  ElMessage({
    message: '正在上传文件...',
    type: 'info',
    duration: 100,
  })
}

const updateChatPreview = (chat, message) => {
  chat.preview = message.contentType === 'text'
    ? (message.content.length > 30 ? message.content.substring(0, 30) + '...' : message.content)
    : message.contentType === 'image' ? '[图片]' : '[文件]'
  chat.lastMessageTime = message.time
  chat.lastMessageType = message.contentType
}

const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 Bytes'
  const k = 1024
  const sizes = ['Bytes', 'KB', 'MB', 'GB']
  const i = Math.floor(Math.log(bytes) / Math.log(k))
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const scrollToBottom = () => {
  if (messagesContainer.value) {
    messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
  }
}

const triggerFileInput = (type) => {
  uploadType.value = type
  if (fileInput.value) {
    fileInput.value.click()
  }
}

const handleFileUpload = (event) => {
  const file = event.target.files[0]
  if (!file) return

  if (uploadType.value === 'image' && !file.type.startsWith('image/')) {
    ElMessage.warning('请选择图片文件')
    return
  }

  const maxSize = uploadType.value === 'image' ? 5 * 1024 * 1024 : 20 * 1024 * 1024
  if (file.size > maxSize) {
    ElMessage.warning(`文件大小不能超过${formatFileSize(maxSize)}`)
    return
  }

  fileToUpload.value = file
  event.target.value = ''
}

const downloadFile = (url) => {
  ElMessage.success('开始下载文件: ' + getFileName(url))
  
}

const downloadImage = () => {
  ElMessage.success('开始下载图片')
  
}

const getFileName = (url) => {
  return url.substring(url.lastIndexOf('/') + 1)
}

const previewImage = (url) => {
  previewImageUrl.value = url
  imagePreviewVisible.value = true
}

const handleMessageCommand = (command, message) => {
  if (command === 'copy') {
    copyMessage(message.content)
  } else if (command === 'recall') {
    recallMessage(message)
  }
}

const copyMessage = async (content) => {
  try {
    await copy(content)
    ElMessage.success('已复制到剪贴板')
  } catch (err) {
    ElMessage.error('复制失败')
  }
}

const recallMessage = (message) => {
  if (!currentChat.value) return

  const index = currentChat.value.messages.findIndex(m => m.id === message.id)
  if (index === -1) return

  if (new Date() - new Date(message.time) > 2 * 60 * 1000) {
    ElMessage.warning('只能撤回2分钟内的消息')
    return
  }

  currentChat.value.messages.splice(index, 1)
  ElMessage.success('消息已撤回')
}

const clearChatHistory = () => {
  if (!currentChat.value) return

  ElMessageBox.confirm(
    '确定要清空当前聊天记录吗？此操作不可撤销',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    currentChat.value.messages = []
    currentChat.value.preview = ''
    ElMessage.success('聊天记录已清空')
  }).catch(() => { })
}

const muteChat = () => {
  ElMessage.success('对话已静音')
}

const handlePlusMenuCommand = (command) => {
  if (command === 'addContact') {
    showNewChatDialog.value = true
  } else if (command === 'createGroup') {
    showCreateGroupDialog.value = true
  }
}

const createNewChat = (contact) => {
  const existingChat = chatHistory.value.find(chat => chat.id === contact.id && !chat.isGroup)

  if (existingChat) {
    currentChatIndex.value = chatHistory.value.indexOf(existingChat)
  } else {
    const newChat = {
      id: contact.id,
      name: contact.name,
      avatar: contact.avatar,
      lastMessageTime: new Date(),
      lastMessageType: 'text',
      preview: '新对话已开始',
      unreadCount: 0,
      online: contact.online,
      lastOnline: contact.lastOnline,
      department: contact.department,
      isGroup: false,
      messages: []
    }

    chatHistory.value.unshift(newChat)
    currentChatIndex.value = 0
  }

  showNewChatDialog.value = false
  newChatSearch.value = ''
}

const handleCloseNewChatDialog = (done) => {
  done()
  newChatSearch.value = ''
  activeTab.value = 'search'
}

const onSearchInput = debounce(() => {
  isSearchingContacts.value = true
  searchPerformed.value = newChatSearch.value.trim() !== ''
  
  
  setTimeout(() => {
    isSearchingContacts.value = false
  }, 300)
}, 300)

const clearSearch = () => {
  newChatSearch.value = ''
  searchPerformed.value = false
}

const highlightMatch = (text, search) => {
  if (!search.trim()) return text
  const regex = new RegExp(`(${search})`, 'gi')
  return text.replace(regex, url => {
    return `<a href="${url}" target="_blank" class="message-link">${url}</a>`
  })
}


const debouncedFilterGroupMembers = debounce(() => {
  filterGroupMembers()
}, 300)

const filterGroupMembers = () => {
  if (!groupMemberSearch.value.trim()) {
    filteredGroupMembers.value = [...contacts.value]
  } else {
    filteredGroupMembers.value = contacts.value.filter(contact =>
      contact.name.toLowerCase().includes(groupMemberSearch.value.toLowerCase())
    )
  }
}

const debouncedFilterAddMembers = debounce(() => {
  filterAddMembers()
}, 300)

const filterAddMembers = () => {
  if (!addMemberSearch.value.trim()) {
    filteredAddMembers.value = [...contacts.value]
  } else {
    filteredAddMembers.value = contacts.value.filter(contact =>
      contact.name.toLowerCase().includes(addMemberSearch.value.toLowerCase())
    )
  }
}

const isMemberSelected = (memberId) => {
  return groupForm.members.includes(memberId)
}

const toggleMemberSelection = (memberId) => {
  const index = groupForm.members.indexOf(memberId)
  if (index > -1) {
    groupForm.members.splice(index, 1)
  } else {
    groupForm.members.push(memberId)
  }
}



const handleAvatarUpload = async ({ file }) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    groupForm.avatar = e.target.result
  }
  reader.readAsDataURL(file)
}

const beforeAvatarUpload = (rawFile) => {
  const isJPG = rawFile.type === 'image/jpeg' || rawFile.type === 'image/png'
  const isLt2M = rawFile.size / 1024 / 1024 < 2

  if (!isJPG) {
    ElMessage.error('上传头像图片只能是 JPG/PNG 格式!')
  }
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!')
  }
  return isJPG && isLt2M
}

const createGroup = async () => {
  const valid = await groupFormRef.value.validate()
  if (!valid) return
  
  isCreatingGroup.value = true
  
  try {
    
    const members = [
      {
        ...currentUser.value,
        role: 'ADMIN',
        isAdmin: true,
        online: true
      }
    ]
    
    
    groupForm.members.forEach(memberId => {
      const contact = contacts.value.find(c => c.id === memberId)
      if (contact) {
        members.push({
          ...contact,
          role: 'MEMBER',
          isAdmin: false
        })
      }
    })

    
    const newGroupId = Date.now()
    
    
    const newGroupChat = {
      id: newGroupId,
      name: groupForm.name,
      avatar: groupForm.avatar,
      description: groupForm.description,
      lastMessageTime: new Date(),
      lastMessageType: 'text',
      preview: `群聊"${groupForm.name}"已创建`,
      unreadCount: 0,
      online: true,
      isGroup: true,
      members,
      messages: [
        {
          id: Date.now(),
          type: 'system',
          contentType: 'text',
          content: `群聊"${groupForm.name}"已创建，${currentUser.value.name}成为群主`,
          time: new Date(),
          status: 'read'
        }
      ]
    }

    
    chatHistory.value.unshift(newGroupChat)
    currentChatIndex.value = 0
    
    
    showCreateGroupDialog.value = false
    groupForm.name = ''
    groupForm.description = ''
    groupForm.avatar = ''
    groupForm.members = []
    groupMemberSearch.value = ''
    
    ElMessage.success(`群聊"${newGroupChat.name}"创建成功`)
  } catch (error) {
    console.error('创建群组失败:', error)
    ElMessage.error('创建群组失败')
  } finally {
    isCreatingGroup.value = false
  }
}

const cancelCreateGroup = () => {
  showCreateGroupDialog.value = false
  groupFormRef.value.resetFields()
  groupForm.members = []
  groupMemberSearch.value = ''
}

const cancelAddMembers = () => {
  showAddMembersDialog.value = false
  membersToAdd.value = []
  addMemberSearch.value = ''
}

const removeFromSelection = (memberId) => {
  const index = membersToAdd.value.findIndex(m => m.id === memberId)
  if (index > -1) {
    membersToAdd.value.splice(index, 1)
  }
}


const getSenderName = (message) => {
  if (!currentChat.value?.isGroup) return ''
  
  if (message.type === 'user') {
    return currentUser.value.name
  }
  
  const member = currentChat.value.members.find(m => m.id === message.senderId)
  return member ? member.name : '未知成员'
}

const getSenderAvatar = (message) => {
  if (!currentChat.value?.isGroup) return currentChat.value?.avatar
  
  if (message.type === 'user') {
    return currentUser.value.avatar
  }
  
  const member = currentChat.value.members.find(m => m.id === message.senderId)
  return member ? member.avatar : currentChat.value.avatar
}

const isCurrentUserAdmin = () => {
  if (!currentChat.value?.isGroup) return false
  
  const currentMember = currentChat.value.members.find(m => m.id === currentUser.value.id)
  return currentMember?.isAdmin || currentMember?.role === 'ADMIN'
}

const canManageMember = (member) => {
  
  
  const currentMember = currentChat.value.members.find(m => m.id === currentUser.value.id)
  
  
  if (!currentMember || (currentMember.role !== 'ADMIN' && !currentMember.isAdmin)) {
    return false
  }
  
  
  if (currentMember.isAdmin) {
    return member.id !== currentUser.value.id 
  }
  
  
  return !member.isAdmin && member.role !== 'ADMIN' && member.id !== currentUser.value.id
}

const handleMemberCommand = (command, member) => {
  if (!currentChat.value) return

  if (command === 'remove') {
    removeGroupMember(member.id)
  } else if (command === 'toggleAdmin') {
    toggleAdminRole(member.id)
  }
}

const removeGroupMember = (memberId) => {
  ElMessageBox.confirm(
    '确定要移除该成员吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    if (currentChat.value) {
      currentChat.value.members = currentChat.value.members.filter(m => m.id !== memberId)
      
      
      const member = contacts.value.find(c => c.id === memberId)
      currentChat.value.messages.push({
        id: Date.now(),
        type: 'system',
        contentType: 'text',
        content: `${member?.name || '某成员'}已被移出群聊`,
        time: new Date(),
        status: 'read'
      })
      
      updateChatPreview(currentChat.value, currentChat.value.messages[currentChat.value.messages.length - 1])
      ElMessage.success('成员已移除')
    }
  }).catch(() => { })
}

const toggleAdminRole = (memberId) => {
  if (!currentChat.value) return

  const member = currentChat.value.members.find(m => m.id === memberId)
  if (member && !member.isAdmin) {
    if (member.role === 'ADMIN') {
      member.role = 'MEMBER'
      ElMessage.success('已取消管理员权限')
      
      
      currentChat.value.messages.push({
        id: Date.now(),
        type: 'system',
        contentType: 'text',
        content: `${member.name}已被取消管理员权限`,
        time: new Date(),
        status: 'read'
      })
    } else {
      member.role = 'ADMIN'
      ElMessage.success('已设置为管理员')
      
      
      currentChat.value.messages.push({
        id: Date.now(),
        type: 'system',
        contentType: 'text',
        content: `${member.name}已成为管理员`,
        time: new Date(),
        status: 'read'
      })
    }
    
    updateChatPreview(currentChat.value, currentChat.value.messages[currentChat.value.messages.length - 1])
  }
}


const isMemberInGroup = (memberId) => {
  return currentChat.value?.members.some(m => m.id === memberId) || 
         membersToAdd.value.some(m => m.id === memberId)
}

const addMemberToGroup = (member) => {
  const index = membersToAdd.value.findIndex(m => m.id === member.id)
  if (index > -1) {
    membersToAdd.value.splice(index, 1)
  } else {
    membersToAdd.value.push({
      ...member,
      role: 'MEMBER',
      isAdmin: false
    })
  }
}

const confirmAddMembers = async () => {
  if (membersToAdd.value.length === 0) {
    showAddMembersDialog.value = false
    return
  }
  
  isAddingMembers.value = true
  
  try {
    if (currentChat.value) {
      
      currentChat.value.members.push(...membersToAdd.value)
      
      
      const memberNames = membersToAdd.value.map(m => m.name).join('、')
      currentChat.value.messages.push({
        id: Date.now(),
        type: 'system',
        contentType: 'text',
        content: `${memberNames}已加入群聊`,
        time: new Date(),
        status: 'read'
      })
      
      updateChatPreview(currentChat.value, currentChat.value.messages[currentChat.value.messages.length - 1])
      ElMessage.success(`成功添加${membersToAdd.value.length}名成员`)
      
      
      membersToAdd.value = []
      showAddMembersDialog.value = false
      addMemberSearch.value = ''
    }
  } catch (error) {
    console.error('添加群成员失败:', error)
    ElMessage.error('添加群成员失败')
  } finally {
    isAddingMembers.value = false
  }
}


watch(currentChat, (newChat) => {
  if (newChat) {
    loadChatMessages()
  }
}, { immediate: true })

onMounted(() => {
  nextTick(() => {
    scrollToBottom()
  })
})
</script>

<style scoped lang="scss">
.modern-chat-app {
  display: flex;
  height: 80vh;
  min-height: 500px;
  width: 100%;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'PingFang SC', 'Microsoft YaHei', sans-serif;
  box-sizing: border-box;

  --primary-color: #3b82f6;
  --primary-hover: #2563eb;
  --text-primary: #1e293b;
  --text-secondary: #64748b;
  --bg-primary: #ffffff;
  --bg-secondary: #f8fafc;
  --border-color: #e2e8f0;
  --online-dot: #10b981;
  --message-user-bg: #3b82f6;
  --message-bot-bg: #ffffff;
  --message-user-color: #ffffff;
  --message-bot-color: #1e293b;

  .sidebar {
    width: 360px;
    background: transparent;
    border-right: 1px solid var(--border-color);
    display: flex;
    flex-direction: column;
    height: 100%;
    flex-shrink: 0;

    &-header {
      padding: 16px 20px;
      border-bottom: 1px solid var(--border-color);
      display: flex;
      align-items: center;
      justify-content: space-between;
      flex-shrink: 0;

      h2 {
        margin: 0;
        font-size: 18px;
        font-weight: 600;
        color: var(--text-primary);
      }

      .header-actions {
        display: flex;
        gap: 8px;
      }

      .el-button {
        color: var(--text-secondary);
        transition: all 0.2s;

        &:hover {
          background: #f1f5f9;
        }
      }

      
      :deep(.el-dropdown-menu) {
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        border: 1px solid var(--border-color);
        padding: 4px;

        .el-dropdown-item {
          display: flex;
          align-items: center;
          gap: 8px;
          padding: 10px 16px;
          border-radius: 6px;
          transition: all 0.2s;

          .el-icon {
            font-size: 16px;
          }

          &:hover {
            background: rgba(59, 130, 246, 0.1);
            color: var(--primary-color);
          }
        }
      }
    }

    .search-box {
      padding: 12px 16px;
      flex-shrink: 0;

      :deep(.el-input__wrapper) {
        background: var(--bg-secondary);
        border-radius: 18px;
        box-shadow: none;
        border: 10 solid var(--border-color);

        &:hover {
          border-color: #cbd5e1;
        }

        .el-input__inner::placeholder {
          color: var(--text-secondary);
        }
      }
    }

    .chat-list {
      flex: 1;
      overflow-y: auto;
      padding: 8px;
      scrollbar-width: thin;
      scrollbar-color: rgba(156, 163, 175, 0.5) transparent;

      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-track {
        background: transparent;
      }

      &::-webkit-scrollbar-thumb {
        background-color: rgba(156, 163, 175, 0.5);
        border-radius: 3px;
      }

      .virtual-scroller {
        height: 100%;
      }

      .chat-item {
        display: flex;
        align-items: center;
        padding: 12px;
        border-radius: 8px;
        cursor: pointer;
        transition: all 0.2s;
        margin-bottom: 4px;

        &:hover {
          background: #f1f5f9;
        }

        &.active {
          background: #e0e7ff;
        }

        .chat-info {
          flex: 1;
          margin-left: 12px;
          overflow: hidden;

          .chat-header {
            display: flex;
            justify-content: space-between;
            align-items: center;

            h3 {
              margin: 0;
              font-size: 14px;
              color: var(--text-primary);
              white-space: nowrap;
              text-overflow: ellipsis;
              overflow: hidden;
              display: flex;
              align-items: center;
              gap: 4px;
            }

            span {
              font-size: 12px;
              color: var(--text-secondary);
            }
          }

          .last-message {
            margin-top: 4px;
            font-size: 13px;
            color: var(--text-secondary);
            white-space: nowrap;
            text-overflow: ellipsis;
            overflow: hidden;

            .el-icon {
              margin-right: 4px;
              font-size: 12px;
            }
          }
        }

        .el-badge {
          margin-left: 6px;
        }

        .el-badge :deep(.el-badge__content) {
          top: 0;
          right: 0;
        }
      }

      .empty-search {
        display: flex;
        align-items: center;
        justify-content: center;
        height: 100%;
      }
    }
  }

  .chat-area {
    flex: 1;
    display: flex;
    flex-direction: column;
    background: var(--bg-secondary);
    height: 100%;
    min-width: 0;

    .empty-chat {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      text-align: center;

      .empty-content {
        .el-icon {
          font-size: 60px;
          color: var(--text-secondary);
          margin-bottom: 20px;
        }

        h3 {
          margin: 0 0 8px;
          font-size: 18px;
          color: var(--text-primary);
        }

        p {
          margin: 0 0 20px;
          color: var(--text-secondary);
          font-size: 14px;
        }

        .el-button {
          margin: 0 4px;
        }

        
        :deep(.el-dropdown-menu) {
          border-radius: 8px;
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
          border: 1px solid var(--border-color);
          padding: 4px;

          .el-dropdown-item {
            display: flex;
            align-items: center;
            gap: 8px;
            padding: 10px 16px;
            border-radius: 6px;
            transition: all 0.2s;

            .el-icon {
              font-size: 16px;
            }

            &:hover {
              background: rgba(59, 130, 246, 0.1);
              color: var(--primary-color);
            }
          }
        }
      }
    }

    .active-chat {
      display: flex;
      flex-direction: column;
      height: 100%;

      .chat-header {
        padding: 16px 24px;
        background: var(--bg-primary);
        border-bottom: 1px solid var(--border-color);
        display: flex;
        justify-content: space-between;
        align-items: center;
        flex-shrink: 0;

        .header-left {
          display: flex;
          align-items: center;

          .el-avatar {
            margin-right: 12px;
          }

          .user-info {
            h3 {
              margin: 0;
              font-size: 16px;
              color: var(--text-primary);
              display: flex;
              align-items: center;
              gap: 8px;
            }

            .online-status {
              margin: 2px 0 0;
              font-size: 12px;
              color: var(--text-secondary);

              .online-dot {
                display: inline-block;
                width: 8px;
                height: 8px;
                background: var(--online-dot);
                border-radius: 50%;
                margin-right: 4px;
              }
            }
          }
        }

        .header-actions {
          .el-button {
            color: var(--text-secondary);
            margin-left: 8px;

            &:hover {
              color: var(--primary-color);
            }
          }
        }
      }

      .messages-container {
        flex: 1;
        display: flex;
        flex-direction: column;
        overflow: hidden;
        position: relative;
        min-height: 0;
      }

      .messages {
        flex: 1;
        overflow-y: auto;
        padding: 20px;
        background: var(--bg-secondary);
        width: 100%;
        box-sizing: border-box;
        min-height: 0;
        scrollbar-width: thin;
        scrollbar-color: rgba(156, 163, 175, 0.5) transparent;

        &::-webkit-scrollbar {
          width: 6px;
        }

        &::-webkit-scrollbar-track {
          background: transparent;
        }

        &::-webkit-scrollbar-thumb {
          background-color: rgba(156, 163, 175, 0.5);
          border-radius: 3px;
        }

        .loading-messages,
        .loading-more {
          display: flex;
          align-items: center;
          justify-content: center;
          padding: 20px;
          color: var(--text-secondary);
          
          .el-icon {
            margin-right: 8px;
          }
        }
      }

      .message-group {
        margin-bottom: 16px;
      }

      .date-divider {
        display: flex;
        justify-content: center;
        margin: 20px 0;
        position: relative;

        &::before {
          content: '';
          position: absolute;
          top: 50%;
          left: 0;
          right: 0;
          height: 1px;
          background: var(--border-color);
          z-index: 1;
        }

        span {
          background: var(--bg-secondary);
          padding: 0 12px;
          font-size: 12px;
          color: var(--text-secondary);
          position: relative;
          z-index: 2;
        }
      }

      .message {
        display: flex;
        margin-bottom: 16px;
        align-items: flex-start;
        opacity: 0;
        transform: translateY(10px);
        animation: fadeIn 0.3s ease forwards;

        &.user {
          flex-direction: row-reverse;

          .message-content {
            align-items: flex-end;
            margin-right: 12px;

            .message-bubble {
              background: var(--message-user-bg);
              color: var(--message-user-color);
              border-radius: 18px 18px 0 18px;

              :deep(a.message-link) {
                color: #cce0ff;
                text-decoration: underline;
              }
            }
          }
        }

        &.bot, &.system {
          .message-content {
            align-items: flex-start;
            margin-left: 12px;

            .message-bubble {
              background: var(--message-bot-bg);
              color: var(--message-bot-color);
              border-radius: 18px 18px 18px 0;
              box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);

              :deep(a.message-link) {
                color: var(--primary-color);
              }
            }
          }
        }
        
        &.system {
          .message-content {
            .message-bubble {
              background: #f0f9ff;
              color: #0284c7;
            }
          }
        }

        .message-content {
          max-width: 70%;
          display: flex;
          flex-direction: column;

          .sender-name {
            font-size: 12px;
            color: var(--text-secondary);
            margin-bottom: 4px;
            margin-left: 4px;
          }

          .message-bubble {
            padding: 12px 16px;
            font-size: 15px;
            line-height: 1.5;
            word-break: break-word;

            img {
              max-width: 300px;
              max-height: 300px;
              border-radius: 8px;
              cursor: pointer;
              transition: transform 0.2s;

              &:hover {
                transform: scale(1.02);
              }
            }

            .file-message {
              display: flex;
              align-items: center;
              padding: 8px;

              .el-icon {
                font-size: 32px;
                color: var(--primary-color);
                margin-right: 12px;
              }

              .file-info {
                flex: 1;

                .file-name {
                  font-weight: 500;
                  margin-bottom: 4px;
                }

                .file-size {
                  font-size: 12px;
                  color: var(--text-secondary);
                }
              }
            }
          }

          .message-meta {
            display: flex;
            align-items: center;
            margin-top: 6px;

            span {
              font-size: 11px;
              color: var(--text-secondary);
            }

            .status-icon {
              font-size: 14px;
              margin-left: 4px;

              &.sending {
                color: var(--text-secondary);
                animation: rotate 1s linear infinite;
              }

              &.sent {
                color: var(--text-secondary);
              }

              &.read {
                color: var(--primary-color);
              }

              &.failed {
                color: #f56c6c;
              }
            }
          }
        }

        .message-actions {
          opacity: 0;
          transition: opacity 0.2s;
          color: var(--text-secondary);
          margin: 0 4px;

          &:hover {
            color: var(--primary-color);
          }
        }

        &:hover .message-actions {
          opacity: 1;
        }
      }

      .input-area {
        padding: 16px;
        background: var(--bg-primary);
        border-top: 1px solid var(--border-color);
        flex-shrink: 0;

        .input-tools {
          display: flex;
          margin-bottom: 8px;

          .el-button {
            color: var(--text-secondary);
            padding: 4px;

            &:hover {
              color: var(--primary-color);
            }
          }
        }

        .input-wrapper {
          position: relative;

          .embedded-input {
            :deep(.el-textarea__inner) {
              border-radius: 8px;
              padding: 12px 60px 12px 16px;
              resize: none;
              border: 1px solid var(--border-color);
              background: var(--bg-secondary);
              box-shadow: none;
              transition: all 0.3s;
              min-height: 48px;

              &:focus {
                border-color: var(--primary-color);
                background: var(--bg-primary);
                box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.2);
              }
            }
          }

          .send-button-container {
            position: absolute;
            right: 6px;
            bottom: 6px;
            width: 36px;
            height: 36px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: var(--bg-secondary);
            border-radius: 50%;
            transition: all 0.3s;

            &::before {
              content: '';
              position: absolute;
              top: -6px;
              right: -6px;
              bottom: -6px;
              left: -6px;
              border: 2px solid var(--bg-secondary);
              border-radius: 50%;
              z-index: -1;
            }
          }

          .send-button {
            width: 32px;
            height: 32px;
            transition: all 0.3s;

            &:disabled {
              opacity: 0.5;
              transform: scale(0.9);
              background-color: #c0c4cc;
              border-color: #c0c4cc;
            }

            &:not(:disabled) {
              &:hover {
                transform: scale(1.05);
                box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);
              }

              &:active {
                transform: scale(0.95);
              }
            }
          }

          &:hover,
          &:focus-within {
            .send-button-container {
              background: var(--bg-primary);

              &::before {
                border-color: var(--bg-primary);
              }
            }
          }

          &:focus-within {
            .send-button-container {
              &::before {
                border-color: rgba(64, 158, 255, 0.2);
              }
            }
          }
        }
      }
    }
  }

  
  .group-badge {
    font-size: 12px;
    background-color: rgba(59, 130, 246, 0.1);
    color: var(--primary-color);
    padding: 1px 6px;
    border-radius: 4px;
    font-weight: normal;
  }
  
  .member-count {
    font-size: 12px;
    color: var(--text-secondary);
    font-weight: normal;
  }
  
  .admin-badge {
    font-size: 12px;
    background-color: #fef3c7;
    color: #d97706;
    padding: 0 4px;
    border-radius: 3px;
    margin-left: 6px;
  }
  
  .group-form {
    .el-form-item {
      margin-bottom: 16px;
    }
    
    .avatar-uploader {
      display: flex;
      align-items: center;
      flex-direction: column;
      
      .upload-avatar {
        cursor: pointer;
        transition: all 0.3s;
        
        &:hover {
          transform: scale(1.05);
        }
      }
      
      .avatar-upload-tip {
        margin-top: 8px;
        font-size: 12px;
        color: var(--text-secondary);
      }
    }
  }
  
  .group-members-list {
    .group-member-item {
      display: flex;
      align-items: center;
      padding: 10px 12px;
      border-radius: 6px;
      cursor: pointer;
      transition: all 0.2s;
      margin-bottom: 4px;
      
      &:hover {
        background-color: var(--bg-secondary);
      }
      
      &.selected {
        background-color: rgba(59, 130, 246, 0.1);
        border-left: 3px solid var(--primary-color);
      }
      
      &.disabled {
        opacity: 0.6;
        cursor: not-allowed;
        
        &:hover {
          background-color: transparent;
        }
      }
      
      .selected-icon {
        margin-left: auto;
        color: var(--primary-color);
      }
      
      .member-info {
        flex: 1;
        margin-left: 8px;
        
        .member-name {
          font-size: 14px;
          font-weight: 500;
        }
        
        .member-status {
          font-size: 12px;
          color: var(--text-secondary);
          margin-top: 2px;
        }
      }
      
      .member-actions {
        color: var(--text-secondary);
        
        &:hover {
          color: var(--primary-color);
        }
      }
    }
  }

  .image-preview-dialog {
    .image-preview-container {
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .preview-image {
        max-width: 100%;
        max-height: 70vh;
        margin-bottom: 20px;
      }
      
      .image-preview-actions {
        display: flex;
        justify-content: center;
      }
    }
  }

  .dark & {
    --primary-color: #3b82f6;
    --primary-hover: #2563eb;
    --text-primary: #e2e8f0;
    --text-secondary: #94a3b8;
    --bg-primary: #1e293b;
    --bg-secondary: #0f172a;
    --border-color: #334155;
    --online-dot: #10b981;
    --message-user-bg: #3b82f6;
    --message-bot-bg: #1e293b;
    --message-user-color: #ffffff;
    --message-bot-color: #e2e8f0;

    .sidebar {
      .chat-list {
        .chat-item {
          &.active {
            background: rgba(59, 130, 246, 0.2);
          }

          &:hover {
            background: rgba(59, 130, 246, 0.1);
          }
        }
      }
    }

    .messages {
      .message-bubble.bot {
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.3);

        :deep(a.message-link) {
          color: #60a5fa;
        }
      }
      
      .message.system .message-bubble {
        background: #0f172a;
        color: #60a5fa;
      }
    }

    .chat-header,
    .input-area {
      border-color: var(--border-color);
    }

    .search-box :deep(.el-input__wrapper) {
      background: var(--bg-secondary);
      border-color: var(--border-color);

      &:hover {
        border-color: #475569;
      }
    }

    .el-input :deep(.el-input__inner),
    .el-textarea :deep(.el-textarea__inner) {
      background: var(--bg-secondary);
      border-color: var(--border-color);
      color: var(--text-primary);

      &::placeholder {
        color: var(--text-secondary);
      }

      &:focus {
        border-color: var(--primary-color);
        box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.2);
      }
    }

    .date-divider::before {
      background: var(--border-color);
    }

    .date-divider span {
      background: var(--bg-secondary);
    }

    .el-button {
      &:hover {
        background: rgba(59, 130, 246, 0.1);
      }
    }

    .el-dropdown-menu {
      background: var(--bg-primary);
      border-color: var(--border-color);

      .el-dropdown-item {
        color: var(--text-primary);

        &:hover {
          background: rgba(59, 130, 246, 0.1);
        }
      }
    }

    
    :deep(.el-dropdown-menu) {
      background: var(--bg-primary);
      border-color: var(--border-color);

      .el-dropdown-item {
        color: var(--text-primary);

        &:hover {
          background: rgba(59, 130, 246, 0.1);
          color: var(--primary-color);
        }
      }
    }

    .el-badge :deep(.el-badge__content) {
      background-color: var(--primary-color);
    }

    .el-dialog {
      background: var(--bg-primary);
      border-color: var(--border-color);

      .el-dialog__title {
        color: var(--text-primary);
      }

      .el-dialog__content {
        color: var(--text-secondary);
      }

      .el-button--primary {
        background-color: var(--primary-color);
        border-color: var(--primary-color);

        &:hover {
          background-color: var(--primary-hover);
          border-color: var(--primary-hover);
        }
      }
    }
    
    .group-badge {
      background-color: rgba(59, 130, 246, 0.2);
    }
    
    .admin-badge {
      background-color: rgba(217, 119, 6, 0.2);
      color: #fbbf24;
    }
    
    .group-member-item {
      &:hover {
        background-color: rgba(59, 130, 246, 0.1);
      }
      
      &.selected {
        background-color: rgba(59, 130, 246, 0.2);
      }
    }
  }
}

.font-medium {
  color: #000000;
}

.custom-contact-card {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 10px;
  cursor: pointer;
  transition: all 0.3s ease;
  background-color: var(--bg-primary);
  color: var(--text-primary);
  margin: 8px auto;
  border: 1px solid var(--border-color);
  position: relative;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

  .contact-avatar {
    margin-right: 16px;
    flex-shrink: 0;
  }

  .contact-info {
    display: flex;
    flex-direction: column;
    justify-content: center;
    flex: 1;
  }

  .contact-name {
    font-size: 16px;
    font-weight: 600;
    margin: 0 0 4px 4px;
    color: var(--text-primary);
    transition: color 0.2s;
  }

  .contact-desc {
    font-size: 14px;
    color: var(--text-secondary);
    margin: 0px 0px 0px 4px;
    transition: color 0.2s;
  }

  .contact-actions {
    opacity: 0;
    transition: opacity 0.2s ease;
    color: var(--text-secondary);

    .el-button {
      padding: 4px;

      &:hover {
        color: var(--primary-color);
      }
    }
  }

  
  &:hover {
    background-color: var(--bg-secondary);
    border-color: rgba(59, 130, 246, 0.3);
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(59, 130, 246, 0.1);

    .contact-actions {
      opacity: 1;
    }
  }

  
  &.active {
    background-color: rgba(59, 130, 246, 0.1);
    border-color: var(--primary-color);

    .contact-name {
      color: var(--primary-color);
    }
  }
}


.dark .custom-contact-card {
  background-color: var(--bg-primary);
  border-color: var(--border-color);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

  &:hover {
    background-color: rgba(59, 130, 246, 0.1);
    border-color: var(--primary-color);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  }

  &.active {
    background-color: rgba(59, 130, 246, 0.2);
  }
}

@keyframes fadeIn {
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
}


.custom-dialog {
  :deep(.el-dialog) {
    border-radius: 12px;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
    border: none;
    overflow: hidden;
  }

  :deep(.el-dialog__header) {
    padding: 20px 24px 16px;
    border-bottom: 1px solid var(--border-color);
    background: var(--bg-primary);
  }

  :deep(.el-dialog__body) {
    padding: 0;
    background: var(--bg-primary);
  }

  :deep(.el-dialog__footer) {
    padding: 16px 24px 20px;
    border-top: 1px solid var(--border-color);
    background: var(--bg-secondary);
  }
}

.dialog-title {
  display: flex;
  align-items: center;
  justify-content: center;

  .dialog-title-text {
    font-size: 18px;
    font-weight: 600;
    color: var(--text-primary);
  }
}

.dialog-content {
  padding: 0;
  max-height: 60vh;
  overflow-y: auto;

  &::-webkit-scrollbar {
    width: 6px;
  }

  &::-webkit-scrollbar-track {
    background: transparent;
  }

  &::-webkit-scrollbar-thumb {
    background-color: rgba(156, 163, 175, 0.5);
    border-radius: 3px;
  }
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;

  .cancel-btn {
    padding: 8px 16px;
    border-radius: 8px;
    font-weight: 500;
    transition: all 0.2s;

    &:hover {
      transform: scale(1.03);
    }

    &:active {
      transform: scale(0.95);
    }
  }

  .create-btn {
    padding: 8px 16px;
    border-radius: 8px;
    font-weight: 500;
    transition: all 0.2s;

    &:hover:not(:disabled) {
      transform: scale(1.03);
    }

    &:active:not(:disabled) {
      transform: scale(0.95);
    }

    &:disabled {
      opacity: 0.6;
      cursor: not-allowed;
    }
  }
}


.dialog-tabs {
  display: flex;
  border-bottom: 1px solid var(--border-color);
  background: var(--bg-primary);

  .tab-item {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    padding: 16px 24px;
    cursor: pointer;
    transition: all 0.2s;
    color: var(--text-secondary);
    font-weight: 500;
    position: relative;

    .el-icon {
      font-size: 18px;
    }

    &:hover {
      color: var(--primary-color);
      background: rgba(59, 130, 246, 0.05);
    }

    &.active {
      color: var(--primary-color);
      font-weight: 600;

      &::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 2px;
        background: var(--primary-color);
        border-radius: 1px;
      }
    }
  }
}

.tab-content {
  padding: 24px;
}


.search-section {
  margin-bottom: 24px;

  .search-input-wrapper {
    .modern-search-input {
      :deep(.el-input__wrapper) {
        border-radius: 12px;
        border: 1px solid var(--border-color);
        background: var(--bg-secondary);
        transition: all 0.2s;
        box-shadow: none;

        &:hover {
          border-color: #cbd5e1;
        }

        &.is-focus {
          border-color: var(--primary-color);
          box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.2);
        }
      }

      :deep(.el-input__inner) {
        padding: 12px 16px;
        font-size: 14px;
      }
    }
  }
}


.empty-state,
.empty-result {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 48px 24px;
  text-align: center;

  .empty-icon {
    width: 64px;
    height: 64px;
    border-radius: 50%;
    background: var(--bg-secondary);
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 16px;

    .el-icon {
      font-size: 28px;
      color: var(--text-secondary);
    }
  }

  .empty-title {
    font-size: 16px;
    font-weight: 600;
    color: var(--text-primary);
    margin: 0 0 8px;
  }

  .empty-desc {
    font-size: 14px;
    color: var(--text-secondary);
    margin: 0 0 20px;
    max-width: 280px;
  }

  .create-contact-btn {
    font-weight: 500;
    transition: all 0.2s;

    &:hover {
      transform: scale(1.03);
    }
  }
}


.loading-state {
  padding: 24px;

  .skeleton-list {
    .skeleton-item {
      display: flex;
      align-items: center;
      padding: 12px 0;
      margin-bottom: 12px;

      .skeleton-avatar {
        width: 48px;
        height: 48px;
        border-radius: 50%;
        background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
        background-size: 200% 100%;
        animation: skeleton-loading 1.5s infinite;
        margin-right: 12px;
      }

      .skeleton-content {
        flex: 1;

        .skeleton-name {
          width: 120px;
          height: 16px;
          background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
          background-size: 200% 100%;
          animation: skeleton-loading 1.5s infinite;
          border-radius: 4px;
          margin-bottom: 8px;
        }

        .skeleton-desc {
          width: 80px;
          height: 12px;
          background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
          background-size: 200% 100%;
          animation: skeleton-loading 1.5s infinite;
          border-radius: 4px;
        }
      }
    }
  }
}

@keyframes skeleton-loading {
  0% {
    background-position: 200% 0;
  }
  100% {
    background-position: -200% 0;
  }
}


.contact-list {
  .modern-contact-card {
    display: flex;
    align-items: center;
    padding: 16px;
    border-radius: 12px;
    cursor: pointer;
    transition: all 0.2s;
    background: var(--bg-primary);
    border: 1px solid var(--border-color);
    margin-bottom: 8px;

    &:hover {
      background: var(--bg-secondary);
      border-color: rgba(59, 130, 246, 0.3);
      transform: translateY(-1px);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
    }

    .contact-avatar {
      position: relative;
      margin-right: 16px;

      .online-indicator {
        position: absolute;
        bottom: 2px;
        right: 2px;
        width: 12px;
        height: 12px;
        border-radius: 50%;
        background: #9ca3af;
        border: 2px solid var(--bg-primary);

        &.online {
          background: #10b981;
        }
      }
    }

    .contact-info {
      flex: 1;

      .contact-header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-bottom: 4px;

        .contact-name {
          font-size: 16px;
          font-weight: 600;
          color: var(--text-primary);
          margin: 0;
        }

        .add-contact-btn {
          padding: 6px 12px;
          border-radius: 6px;
          font-size: 12px;
          font-weight: 500;
          transition: all 0.2s;

          &:hover {
            transform: scale(1.05);
          }
        }
      }

      .contact-details {
        display: flex;
        align-items: center;
        gap: 8px;

        .online-status {
          font-size: 12px;
          color: var(--text-secondary);

          &.online {
            color: #10b981;
          }
        }

        .department {
          font-size: 12px;
          color: var(--text-secondary);
        }
      }
    }
  }
}


.qr-section,
.contacts-section {
  padding: 48px 24px;
  text-align: center;

  .qr-container,
  .contacts-placeholder {
    .qr-placeholder,
    .contacts-placeholder {
      width: 120px;
      height: 120px;
      border-radius: 12px;
      background: var(--bg-secondary);
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      margin: 0 auto 16px;

      .el-icon {
        font-size: 32px;
        color: var(--text-secondary);
        margin-bottom: 8px;
      }

      p {
        font-size: 14px;
        color: var(--text-secondary);
        margin: 0;
      }
    }
  }

  .qr-tip {
    font-size: 14px;
    color: var(--text-secondary);
    margin: 0;
  }
}


.modern-group-form {
  .form-section {
    padding: 24px;
    border-bottom: 1px solid var(--border-color);

    &:last-child {
      border-bottom: none;
    }
  }

  .form-item {
    margin-bottom: 24px;

    &:last-child {
      margin-bottom: 0;
    }

    .form-label {
      display: block;
      font-size: 14px;
      font-weight: 600;
      color: var(--text-primary);
      margin-bottom: 8px;
    }

    .input-wrapper {
      .modern-input,
      .modern-textarea {
        :deep(.el-input__wrapper) {
          border-radius: 8px;
          border: 1px solid var(--border-color);
          background: var(--bg-secondary);
          transition: all 0.2s;
          box-shadow: none;

          &:hover {
            border-color: #cbd5e1;
          }

          &.is-focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.2);
          }
        }

        :deep(.el-input__inner),
        :deep(.el-textarea__inner) {
          padding: 12px 16px;
          font-size: 14px;
        }
      }

      .input-tip {
        font-size: 12px;
        color: #f53f3f;
        margin-top: 4px;
      }
    }
  }

  .avatar-upload-section {
    .modern-avatar-uploader {
      :deep(.el-upload-dragger) {
        border: 2px dashed var(--border-color);
        border-radius: 12px;
        background: var(--bg-secondary);
        transition: all 0.2s;

        &:hover {
          border-color: var(--primary-color);
          background: rgba(59, 130, 246, 0.05);
        }
      }

              .avatar-upload-area {
          display: flex;
          flex-direction: column;
          align-items: center;
          padding: 16px;

          .upload-avatar {
            margin-bottom: 12px;
            transition: all 0.2s;

            &:hover {
              transform: scale(1.05);
            }
          }

          .upload-tip {
            text-align: center;

            p {
              margin: 0 0 4px;
              font-size: 13px;
              color: var(--text-primary);
            }

            .upload-hint {
              font-size: 11px;
              color: var(--text-secondary);
            }
          }
        }
    }
  }

  .members-section {
    .members-search {
      margin-bottom: 16px;

      .modern-search-input {
        :deep(.el-input__wrapper) {
          border-radius: 8px;
          border: 1px solid var(--border-color);
          background: var(--bg-secondary);
          transition: all 0.2s;
          box-shadow: none;

          &:hover {
            border-color: #cbd5e1;
          }

          &.is-focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.2);
          }
        }
      }
    }

    .members-list {
      max-height: 200px;
      overflow-y: auto;
      border: 1px solid var(--border-color);
      border-radius: 8px;
      background: var(--bg-secondary);

      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-track {
        background: transparent;
      }

      &::-webkit-scrollbar-thumb {
        background-color: rgba(156, 163, 175, 0.5);
        border-radius: 3px;
      }

      .modern-member-item {
        display: flex;
        align-items: center;
        padding: 12px 16px;
        cursor: pointer;
        transition: all 0.2s;
        border-bottom: 1px solid var(--border-color);

        &:last-child {
          border-bottom: none;
        }

        &:hover {
          background: rgba(59, 130, 246, 0.05);
        }

        &.selected {
          background: rgba(59, 130, 246, 0.1);
          border-left: 3px solid var(--primary-color);
        }

        .member-avatar {
          position: relative;
          margin-right: 12px;

          .online-indicator {
            position: absolute;
            bottom: 2px;
            right: 2px;
            width: 10px;
            height: 10px;
            border-radius: 50%;
            background: #9ca3af;
            border: 2px solid var(--bg-secondary);

            &.online {
              background: #10b981;
            }
          }
        }

        .member-info {
          flex: 1;
          display: flex;
          flex-direction: column;

          .member-name {
            font-size: 14px;
            font-weight: 500;
            color: var(--text-primary);
            margin-bottom: 2px;
          }

          .member-desc {
            font-size: 12px;
            color: var(--text-secondary);
          }
        }

        .member-action {
          .selected-icon {
            color: var(--primary-color);
            font-size: 16px;
          }
        }
      }
    }

    .members-summary {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 12px;
      padding: 8px 0;

      .selected-count {
        font-size: 12px;
        color: var(--text-secondary);
      }

      .min-requirement {
        font-size: 12px;
        color: #f53f3f;
      }
    }
  }
}


.members-management-section {
  padding: 24px;

  .members-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20px;

          .header-info {
        .members-section-title {
          font-size: 16px;
          font-weight: 600;
          color: var(--text-primary);
          margin: 0 0 4px;
        }

      .section-desc {
        font-size: 13px;
        color: var(--text-secondary);
        margin: 0;
      }
    }

    .add-members-btn {
      padding: 8px 16px;
      border-radius: 8px;
      font-weight: 500;
      transition: all 0.2s;

      &:hover {
        transform: scale(1.03);
      }
    }
  }

  .members-list-container {
    max-height: 400px;
    overflow-y: auto;

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: transparent;
    }

    &::-webkit-scrollbar-thumb {
      background-color: rgba(156, 163, 175, 0.5);
      border-radius: 3px;
    }

    .empty-members {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 48px 24px;
      text-align: center;

      .empty-icon {
        width: 64px;
        height: 64px;
        border-radius: 50%;
        background: var(--bg-secondary);
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 16px;

        .el-icon {
          font-size: 28px;
          color: var(--text-secondary);
        }
      }

      .empty-title {
        font-size: 16px;
        font-weight: 600;
        color: var(--text-primary);
        margin: 0 0 8px;
      }

      .empty-desc {
        font-size: 14px;
        color: var(--text-secondary);
        margin: 0;
      }
    }

    .modern-members-list {
      .modern-member-card {
        display: flex;
        align-items: center;
        padding: 16px;
        border-radius: 12px;
        background: var(--bg-primary);
        border: 1px solid var(--border-color);
        margin-bottom: 8px;
        transition: all 0.2s;

        &:hover {
          background: var(--bg-secondary);
          border-color: rgba(59, 130, 246, 0.3);
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
        }

        .member-avatar {
          position: relative;
          margin-right: 16px;

          .online-indicator {
            position: absolute;
            bottom: 2px;
            right: 2px;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background: #9ca3af;
            border: 2px solid var(--bg-primary);

            &.online {
              background: #10b981;
            }
          }
        }

        .member-info {
          flex: 1;

          .member-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 4px;

            .member-name {
              font-size: 16px;
              font-weight: 600;
              color: var(--text-primary);
              margin: 0;
              display: flex;
              align-items: center;
              gap: 8px;
            }

            .member-actions {
              .member-action-btn {
                color: var(--text-secondary);
                transition: all 0.2s;

                &:hover {
                  color: var(--primary-color);
                }
              }
            }
          }

          .member-details {
            display: flex;
            align-items: center;
            gap: 12px;

            .online-status {
              font-size: 12px;
              color: var(--text-secondary);

              &.online {
                color: #10b981;
              }
            }

            .join-time {
              font-size: 12px;
              color: var(--text-secondary);
            }
          }
        }
      }
    }
  }
}


.role-badge {
  font-size: 11px;
  padding: 2px 6px;
  border-radius: 4px;
  font-weight: 500;

  &.owner {
    background-color: #fef3c7;
    color: #d97706;
  }

  &.admin {
    background-color: #dbeafe;
    color: #2563eb;
  }

  &.member {
    background-color: #f1f5f9;
    color: #64748b;
  }
}


.add-count-badge {
  font-size: 12px;
  background-color: rgba(59, 130, 246, 0.1);
  color: var(--primary-color);
  padding: 4px 8px;
  border-radius: 12px;
  font-weight: 500;
  margin-left: 8px;
}


.add-members-section {
  padding: 24px;

  .search-section {
    margin-bottom: 20px;

    .search-input-wrapper {
      .modern-search-input {
        :deep(.el-input__wrapper) {
          border-radius: 12px;
          border: 1px solid var(--border-color);
          background: var(--bg-secondary);
          transition: all 0.2s;
          box-shadow: none;

          &:hover {
            border-color: #cbd5e1;
          }

          &.is-focus {
            border-color: var(--primary-color);
            box-shadow: 0 0 0 1px rgba(59, 130, 246, 0.2);
          }
        }
      }
    }
  }

  .members-selection-container {
    max-height: 300px;
    overflow-y: auto;
    margin-bottom: 20px;

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-track {
      background: transparent;
    }

    &::-webkit-scrollbar-thumb {
      background-color: rgba(156, 163, 175, 0.5);
      border-radius: 3px;
    }

    .empty-search-result,
    .empty-contacts {
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      padding: 48px 24px;
      text-align: center;

      .empty-icon {
        width: 64px;
        height: 64px;
        border-radius: 50%;
        background: var(--bg-secondary);
        display: flex;
        align-items: center;
        justify-content: center;
        margin-bottom: 16px;

        .el-icon {
          font-size: 28px;
          color: var(--text-secondary);
        }
      }

      .empty-title {
        font-size: 16px;
        font-weight: 600;
        color: var(--text-primary);
        margin: 0 0 8px;
      }

      .empty-desc {
        font-size: 14px;
        color: var(--text-secondary);
        margin: 0;
      }
    }

    .modern-add-members-list {
      .modern-add-member-card {
        display: flex;
        align-items: center;
        padding: 16px;
        border-radius: 12px;
        background: var(--bg-primary);
        border: 1px solid var(--border-color);
        margin-bottom: 8px;
        cursor: pointer;
        transition: all 0.2s;
        position: relative;

        &:hover:not(.disabled) {
          background: var(--bg-secondary);
          border-color: rgba(59, 130, 246, 0.3);
          transform: translateY(-1px);
          box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
        }

        &.selected {
          background: rgba(59, 130, 246, 0.1);
          border-color: var(--primary-color);
        }

        &.disabled {
          opacity: 0.6;
          cursor: not-allowed;

          &:hover {
            background: var(--bg-primary);
            transform: none;
            box-shadow: none;
          }
        }

        .member-avatar {
          position: relative;
          margin-right: 16px;

          .online-indicator {
            position: absolute;
            bottom: 2px;
            right: 2px;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background: #9ca3af;
            border: 2px solid var(--bg-primary);

            &.online {
              background: #10b981;
            }
          }
        }

        .member-info {
          flex: 1;

          .member-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 4px;

            .member-name {
              font-size: 16px;
              font-weight: 600;
              color: var(--text-primary);
              margin: 0;
            }

            .member-action {
              .selected-icon {
                color: var(--primary-color);
                font-size: 18px;
              }

              .add-icon {
                color: var(--text-secondary);
                font-size: 18px;
                transition: all 0.2s;
              }
            }
          }

          .member-details {
            display: flex;
            align-items: center;
            gap: 12px;

            .online-status {
              font-size: 12px;
              color: var(--text-secondary);

              &.online {
                color: #10b981;
              }
            }

            .department {
              font-size: 12px;
              color: var(--text-secondary);
            }
          }
        }

        .selection-overlay {
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(0, 0, 0, 0.05);
          display: flex;
          align-items: center;
          justify-content: center;
          border-radius: 12px;

          .already-in-group {
            font-size: 12px;
            color: var(--text-secondary);
            background: var(--bg-primary);
            padding: 4px 8px;
            border-radius: 4px;
          }
        }
      }
    }
  }

  .selected-members-preview {
    border-top: 1px solid var(--border-color);
    padding-top: 20px;

    .preview-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 12px;

      .preview-title {
        font-size: 14px;
        font-weight: 600;
        color: var(--text-primary);
        margin: 0;
      }

      .clear-selection-btn {
        font-size: 12px;
        color: var(--text-secondary);
        padding: 4px 8px;

        &:hover {
          color: #f56c6c;
        }
      }
    }

    .selected-members-list {
      display: flex;
      flex-wrap: wrap;
      gap: 8px;

      .selected-member-tag {
        display: flex;
        align-items: center;
        gap: 6px;
        padding: 6px 10px;
        background: rgba(59, 130, 246, 0.1);
        border: 1px solid rgba(59, 130, 246, 0.2);
        border-radius: 16px;
        transition: all 0.2s;

        &:hover {
          background: rgba(59, 130, 246, 0.15);
        }

        .member-name {
          font-size: 12px;
          color: var(--primary-color);
          font-weight: 500;
        }

        .remove-btn {
          padding: 2px;
          color: var(--text-secondary);

          &:hover {
            color: #f56c6c;
          }
        }
      }
    }
  }
}


.dark {
  .custom-dialog {
    :deep(.el-dialog) {
      background: var(--bg-primary);
      border-color: var(--border-color);
    }
  }

  .dialog-tabs {
    .tab-item {
      &:hover {
        background: rgba(59, 130, 246, 0.1);
      }
    }
  }

  .modern-contact-card {
    &:hover {
      background: rgba(59, 130, 246, 0.1);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }
  }

  .modern-member-item {
    &:hover {
      background: rgba(59, 130, 246, 0.1);
    }

    &.selected {
      background: rgba(59, 130, 246, 0.2);
    }
  }

  .skeleton-item {
    .skeleton-avatar,
    .skeleton-name,
    .skeleton-desc {
      background: linear-gradient(90deg, #374151 25%, #4b5563 50%, #374151 75%);
    }
  }

  
  .role-badge {
    &.owner {
      background-color: rgba(217, 119, 6, 0.2);
      color: #fbbf24;
    }

    &.admin {
      background-color: rgba(59, 130, 246, 0.2);
      color: #60a5fa;
    }

    &.member {
      background-color: rgba(100, 116, 139, 0.2);
      color: #94a3b8;
    }
  }

  
  .modern-member-card,
  .modern-add-member-card {
    &:hover {
      background: rgba(59, 130, 246, 0.1);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
    }

    &.selected {
      background: rgba(59, 130, 246, 0.2);
    }
  }

  
  .selected-member-tag {
    background: rgba(59, 130, 246, 0.2);
    border-color: rgba(59, 130, 246, 0.3);

    &:hover {
      background: rgba(59, 130, 246, 0.25);
    }
  }
}
</style>