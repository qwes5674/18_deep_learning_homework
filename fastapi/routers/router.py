from fastapi import Request, UploadFile, File, APIRouter
from fastapi.responses import HTMLResponse
from fastapi.staticfiles import StaticFiles
from fastapi.templating import Jinja2Templates
from yolov5_model import detect_people
import shutil

# APIRouter 인스턴스 생성
router = APIRouter()

# 템플릿 설정
templates = Jinja2Templates(directory="templates")

# images 폴더를 정적 파일로 제공
router.mount("/images", StaticFiles(directory="images"), name="images")


@router.get("/", response_class=HTMLResponse)
def read_index(request: Request):
    return templates.TemplateResponse("index.html", {"request": request})

@router.post("/analyze/upload/")
async def upload_file(file: UploadFile = File(...)):
    
    # 파일 저장 경로
    file_location = f"images/{file.filename}"
    
    # 업로드된 파일 저장
    with open(file_location, "wb") as file_object:
        shutil.copyfileobj(file.file, file_object)
        
    # 분석
    result = detect_people(file_location)
    
    return result  
