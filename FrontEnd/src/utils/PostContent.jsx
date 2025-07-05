import parse from 'html-react-parser';

const PostContent = ({ content }) => {
    const html = content.replace(/\n/g, '<br/>');
    return parse(html);
}

export default PostContent
