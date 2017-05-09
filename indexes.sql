CREATE INDEX csipdaystopping_daycourse_loid_idx
    ON public.csipdaystopping USING btree
    (daycourse_loid)
    TABLESPACE pg_default;

CREATE INDEX csipdaystopping_daycourse_strapdate_idx
    ON public.csipdaystopping USING btree
    (strapdate)
    TABLESPACE pg_default;

CREATE INDEX csipdaystopping_delaysec_idx
    ON public.csipdaystopping USING btree
    (delaysec)
    TABLESPACE pg_default;

CREATE INDEX csipdaystopping_line_loid_idx
    ON public.csipdaystopping USING btree
    (line_loid)
    TABLESPACE pg_default;

CREATE INDEX csipdaystopping_ollvdls_idx
    ON public.csipdaystopping USING btree
    (strapdate, vehicle COLLATE pg_catalog."default", line_loid, daycourse_loid, orderincourse)
    TABLESPACE pg_default;

CREATE INDEX csipdaystopping_orderincourse_idx
    ON public.csipdaystopping USING btree
    (orderincourse)
    TABLESPACE pg_default;

CREATE INDEX csipdaystopping_realarrival_idx
    ON public.csipdaystopping USING btree
    (realarrival)
    TABLESPACE pg_default;

CREATE INDEX csipdaystopping_stoppoint_idx
    ON public.csipdaystopping USING btree
    (stoppoint_loid)
    TABLESPACE pg_default;

CREATE INDEX csipdaystopping_vehicle_idx
    ON public.csipdaystopping USING btree
    (vehicle COLLATE pg_catalog."default")
    TABLESPACE pg_default;