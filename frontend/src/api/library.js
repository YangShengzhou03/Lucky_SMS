import request from '@/utils/request'

export const searchBooks = (params) => {
  return request({
    url: '/student/library/search',
    method: 'get',
    params
  })
}

export const getBookDetail = (bookId) => {
  return request({
    url: `/student/library/detail/${bookId}`,
    method: 'get'
  })
}

export const reserveBook = (bookId) => {
  return request({
    url: '/student/library/reserve',
    method: 'post',
    data: { bookId }
  })
}

export const getMyBorrows = () => {
  return request({
    url: '/student/library/borrows',
    method: 'get'
  })
}

export const renewBook = (borrowId) => {
  return request({
    url: '/student/library/renew',
    method: 'post',
    data: { borrowId }
  })
}

export const getRecommendedBooks = () => {
  return request({
    url: '/student/library/recommended',
    method: 'get'
  })
}