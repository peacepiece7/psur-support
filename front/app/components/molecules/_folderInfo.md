# FolderInfo

2~3개 정도의 기능이 합쳐진 컴포넌트를 추가해주세요

composable(hook)이 포함될 수 있고 prop을 받을 수 있습니다
store, fetcher를 내부에서 선언하면 안되고 외부에서 주입 받아야합니다
미디워쿼리 또한 외부에서 제어할 수 있어야합니다
특정 dto에 종속되지 않도록 만들어주세요

에를들면 다음과 같습니다

- form + input
- form + textarea
- breadCrumb
- causel
- imageCropper
