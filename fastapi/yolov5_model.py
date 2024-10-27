# yolov5_model.py
import cv2
import torch
import time

# YOLOv5 모델 로드 (사람 인식용)
model = torch.hub.load('ultralytics/yolov5', 'yolov5s', pretrained=True)

def detect_people(img_path: str):
    img = cv2.imread(img_path)

    if img is None:
        return {"error": "이미지 파일을 찾을 수 없습니다."}

    # 이미지에서 사람 인식
    results = model(img)

    # 인식 결과에서 사람만 필터링
    people = results.pred[0]
    person_count = sum(1 for *box, conf, cls in people.tolist() if conf > 0.1 and int(cls) == 0)

    # 혼잡도 정하기
    congestion = ""
    if person_count < 4:
        congestion = "원할"
    elif person_count < 12:
        congestion = "조금 혼잡"
    else:
        congestion = "혼잡"

    # 결과 이미지 저장
    timestamp = int(time.time())  # 현재 시간을 초 단위로 가져오기
    output_path = f'images/output_image_{timestamp}.png'
    img_with_detections = results.render()[0]
    cv2.imwrite(output_path, img_with_detections)

    return {
        "person_count": person_count,
        "congestion": congestion,
        "image": f"/images/output_image_{timestamp}.png"
    }
