import request from '@/utils/request'

/**
 * 搜索图书
 * @param {Object} params - 搜索参数
 * @param {string} params.query - 搜索关键词
 * @param {string} params.type - 图书类型（可选）
 * @param {string} params.sort - 排序方式（可选）
 * @param {string} params.location - 馆藏位置（可选）
 * @param {number} params.page - 页码
 * @param {number} params.size - 每页大小
 * @returns {Promise} 搜索结果
 */
export const searchBooks = (params) => {
  return request({
    url: '/student/library/search',
    method: 'get',
    params
  })
}

/**
 * 获取图书详情
 * @param {number} bookId - 图书ID
 * @returns {Promise} 图书详情
 */
export const getBookDetail = (bookId) => {
  return request({
    url: `/student/library/detail/${bookId}`,
    method: 'get'
  })
}

/**
 * 预约图书
 * @param {number} bookId - 图书ID
 * @returns {Promise} 预约结果
 */
export const reserveBook = (bookId) => {
  return request({
    url: '/student/library/reserve',
    method: 'post',
    data: { bookId }
  })
}

/**
 * 获取我的借阅记录
 * @returns {Promise} 借阅记录
 */
export const getMyBorrows = () => {
  return request({
    url: '/student/library/borrows',
    method: 'get'
  })
}

/**
 * 续借图书
 * @param {number} borrowId - 借阅记录ID
 * @returns {Promise} 续借结果
 */
export const renewBook = (borrowId) => {
  return request({
    url: '/student/library/renew',
    method: 'post',
    data: { borrowId }
  })
}

/**
 * 获取热门推荐图书
 * @returns {Promise} 热门推荐图书列表
 */
export const getRecommendedBooks = () => {
  return request({
    url: '/student/library/recommended',
    method: 'get'
  })
}