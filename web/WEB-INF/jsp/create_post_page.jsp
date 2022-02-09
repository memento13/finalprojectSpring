<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>

    <meta charset="UTF-8">
    <title>post</title>

    <!-- include libraries(jQuery, bootstrap) -->
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.5.1.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>

    <!-- include summernote css/js -->
    <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote.min.js"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <form method="post" action="create-post/create.pknu" onsubmit="saveContent()" >
        <input type="hidden" name="project_id" value="${project_id}">
        <input type="hidden" name="party_id" value="${party_id}">
        <div class="row">
            <label for="title" class="col-sm-2">제목 :</label>
            <input type="text" name="title" id="title" class="form-control col-sm-10">
        </div>
            <textarea name="content" style="display: none"></textarea>
        <div id="summernote">서머노트테스트</div>
        <input type="submit" class="btn btn-primary">
    </form>

</div>


<script>
    $(document).ready(function(){

        //썸머노트에 값넣기
        $('#summernote').summernote('code', '<p></p>');

        //위와 같이 값을 먼저 넣어준 후 초기화를 시킨다. 그럼 아래와 같이 입력이 된다.
        //초기화
        $('#summernote').summernote({
            height : 400, // set editor height
            minHeight : null, // set minimum height of editor
            maxHeight : null, // set maximum height of editor
            focus : true,
            lang : 'ko-KR' // 기본 메뉴언어 US->KR로 변경
        });



        //저장버튼 클릭
        $(document).on('click', '#saveBtn', function () {
            saveContent();

        });
    });

    //데이터 저장
    function saveContent() {
        //값 가져오기
        $('textarea[name="content"]').val(encodeURIComponent($('#summernote').summernote('code')));
    }



</script>
</body>
</html>
