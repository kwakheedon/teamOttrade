import React from 'react'

//게시판(커뮤, qna, 공지사항)아래 번호를 누르면 페이지가 나오게하는 컴포넌트
// const PageNav = () => {
//   return (
//     <div>
//       <button className='pageBtn'></button>
//     </div>
//   )
// }

// export default PageNav

const PageNav = ({ postsPerPage, totalPosts, paginate, currentPage }) => {
  const pageNumbers = [];

  for (let i = 1; i <= Math.ceil(totalPosts / postsPerPage); i++) {
    pageNumbers.push(i);
  }

  return (
    <nav className="page-nav-container">
      <ul className="pagination">
        {pageNumbers.map(number => (
          <li key={number} className="page-item">
            <button
              onClick={() => paginate(number)}
              className={`pageBtn ${currentPage === number ? 'active' : ''}`}
            >
              {number}
            </button>
          </li>
        ))}
      </ul>
    </nav>
  );
};

export default PageNav;