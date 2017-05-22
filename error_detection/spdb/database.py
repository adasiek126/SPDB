from sqlalchemy import create_engine
from sqlalchemy.engine.url import URL
from sqlalchemy.sql import text
from datatypes import *
from analyzes import *


class Database:
    def __init__(self, database_name = "back", user = "postgres", password = "postgres", host = "localhost", port = 5432):
        self.engine = create_engine(URL("postgres", username=user, password=password, host=host, port=port, database=database_name), echo=False)
        self.conn = self.engine.connect()

    def getTraceForDayCourse(self, id):
        s = text("select latitude, longitude, gentime from csiptrace where daycourseid = :id_day_course order by gentime")
        result = self.conn.execute(s, id_day_course=str(id))
        list = []
        for row in result:
            trace_result = TraceResult(Point(float(row['latitude']), float(row['longitude'])), row['gentime'])
            list.append(trace_result)
        result.close()
        return list



    def getAllDayCoursesForVariant(self, id):
        s = text("""SELECT DISTINCT csipdaycourse.loid
                    FROM csipdaycourse
                    JOIN csipcourse ON csipdaycourse.course_loid = csipcourse.loid
                    JOIN csipvariant ON csipvariant.loid = csipcourse.variant_loid
                    WHERE csipvariant.loid = :variant_id""")
        result = self.conn.execute(s, variant_id=str(id))
        list = []  
        for row in result: 
            list.append(row['loid']) 
        result.close()
        return list    

    def getVariantsForStop(self, id):

        s = text("""SELECT DISTINCT variant_loid
                    FROM csipvariantstopping
                    WHERE stoppoint_loid = :variant_id""")
        result = self.conn.execute(s, variant_id=str(id))
        list = []  
        for row in result: 
            list.append(row['variant_loid']) 
        result.close()
        return list  

    def getStopPoint(self, id):
        s = text("""SELECT latitude, longitude
                    FROM csipstoppoint
                    WHERE loid = :stop_id""")
        result = self.conn.execute(s, stop_id=str(id)).fetchone()
        point = Point(float(result['latitude']), float(result['longitude']))
        return point

    def getAllStopPoints(self):
        s = text("""SELECT loid, latitude, longitude, name
                    FROM csipstoppoint
                    ORDER BY loid""")
        result = self.conn.execute(s)
        list = []  
        for row in result: 
            if(row['latitude'] is not None and row['longitude'] is not None):
                list.append(StopPoint(row['loid'], Point(float(row['latitude']), float(row['longitude'])), row['name'], None))
        result.close()
        return list

    def getLinesWithStops(self):
        s = text("""SELECT DISTINCT csipstoppoint.loid, csipline.name
                    FROM csipvariantstopping
                    JOIN csipstoppoint ON csipstoppoint.loid = csipvariantstopping.stoppoint_loid
                    JOIN csipvariant ON csipvariant.loid = csipvariantstopping.variant_loid
                    JOIN csipline ON csipline.loid = csipvariant.line_loid
                    ORDER BY csipstoppoint.loid, csipline.name""")
        result = self.conn.execute(s)
        list = []  
        for row in result: 
            list.append(LinesWithStops(row['loid'], row['name']))
        result.close()
        return list


        
