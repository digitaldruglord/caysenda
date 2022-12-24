const findAllByKeywordService = async (keyword)=>{
    const responses = await fetch("/ajax/search?keyword="+keyword,{method:"GET"});
    return await responses.json();
}