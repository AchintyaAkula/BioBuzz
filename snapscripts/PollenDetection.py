import cv2
import numpy as np

lower = np.array([27, 75, 75])
upper = np.array([55, 255, 255])
tolerance = 20

def runPipeline(image, llrobot):

    largest = np.array([], dtype=np.int32)
    lldata = []

    hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)

    mask = cv2.inRange(hsv, lower, upper)
    kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (tolerance, tolerance))
    morphed = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel, )

    contours, hierarchy = cv2.findContours(morphed, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    if contours:
        largest = max(contours, key=cv2.contourArea)

        area = cv2.contourArea(largest)
        cv2.drawContours(image, [largest], -1, (0, 255, 0), 2)
        x, y, w, h = cv2.boundingRect(largest)

        cv2.rectangle(image, (x, y), (x + w, y + h), (0, 0, 255), 2)

    return largest, image, []
